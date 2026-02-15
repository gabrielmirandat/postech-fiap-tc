package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	menuhttp "github.com/gabriel/menu/internal/adapter/http"
	"github.com/gabriel/menu/internal/adapter/messaging"
	"github.com/gabriel/menu/internal/adapter/repository"
	"github.com/gabriel/menu/internal/application"
	"github.com/gabriel/menu/internal/config"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func main() {
	cfg := config.Load()
	gin.SetMode(gin.ReleaseMode)

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	client, err := mongo.Connect(ctx, options.Client().ApplyURI(cfg.MongoURI))
	if err != nil {
		log.Fatalf("mongodb connect: %v", err)
	}
	defer func() {
		if err := client.Disconnect(context.Background()); err != nil {
			log.Printf("mongodb disconnect: %v", err)
		}
	}()

	db := client.Database(cfg.MongoDB)
	ingredientRepo := repository.NewMongoIngredientRepository(db)
	productRepo := repository.NewMongoProductRepository(db)

	productPub := &messaging.NoopProductPublisher{}
	ingredientPub := &messaging.NoopIngredientPublisher{}

	ingredientSvc := application.NewIngredientService(ingredientRepo, ingredientPub)
	productSvc := application.NewProductService(productRepo, ingredientSvc, productPub)

	router := menuhttp.NewRouter(productSvc, ingredientSvc)
	srv := &http.Server{
		Addr:    ":" + cfg.HTTPPort,
		Handler: router,
	}

	go func() {
		log.Printf("menu server listening on :%s", cfg.HTTPPort)
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("listen: %v", err)
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("shutting down...")
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer shutdownCancel()
	if err := srv.Shutdown(shutdownCtx); err != nil {
		log.Printf("server shutdown: %v", err)
	}
	log.Println("server exited")
}
