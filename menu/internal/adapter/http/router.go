package http

import (
	"github.com/gin-gonic/gin"
	"github.com/gabriel/menu/internal/application"
)

func NewRouter(
	productSvc *application.ProductService,
	ingredientSvc *application.IngredientService,
) *gin.Engine {
	r := gin.Default()
	products := NewProductsHandler(productSvc)
	ingredients := NewIngredientsHandler(ingredientSvc)

	// Products (OAS paths)
	r.GET("/products", products.FindProductsByQuery)
	r.POST("/products", products.AddProduct)
	r.GET("/products/:productId", products.GetProductByID)
	r.DELETE("/products/:productId", products.DeleteProduct)

	// Ingredients
	r.GET("/ingredients", ingredients.FindIngredientsByQuery)
	r.POST("/ingredients", ingredients.AddIngredient)
	r.GET("/ingredients/:ingredientId", ingredients.GetIngredientByID)

	return r
}
