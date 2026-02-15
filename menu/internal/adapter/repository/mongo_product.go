package repository

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"time"
)

type MongoProductRepository struct {
	coll *mongo.Collection
}

func NewMongoProductRepository(db *mongo.Database) *MongoProductRepository {
	return &MongoProductRepository{coll: db.Collection("products")}
}

type productDoc struct {
	ID          string    `bson:"_id"`
	Name        string    `bson:"name"`
	Description string    `bson:"description"`
	Category    string    `bson:"category"`
	Price       float64   `bson:"price"`
	Image       string    `bson:"image"`
	Ingredients []string  `bson:"ingredients"`
	CreatedAt   time.Time `bson:"createdAt"`
	UpdatedAt   time.Time `bson:"updatedAt"`
}

func (r *MongoProductRepository) Save(ctx context.Context, p *domain.Product) error {
	now := time.Now()
	p.CreatedAt = now
	p.UpdatedAt = now
	doc := productDoc{
		ID:          p.ID,
		Name:        p.Name,
		Description: p.Description,
		Category:    p.Category.String(),
		Price:       p.Price,
		Image:       p.Image,
		Ingredients: p.Ingredients,
		CreatedAt:   now,
		UpdatedAt:   now,
	}
	_, err := r.coll.InsertOne(ctx, doc)
	return err
}

func (r *MongoProductRepository) GetByID(ctx context.Context, id string) (*domain.Product, error) {
	var doc productDoc
	err := r.coll.FindOne(ctx, bson.M{"_id": id}).Decode(&doc)
	if err == mongo.ErrNoDocuments {
		return nil, domain.ErrProductNotFound
	}
	if err != nil {
		return nil, err
	}
	return docToProduct(&doc), nil
}

func (r *MongoProductRepository) SearchByCategory(ctx context.Context, cat domain.Category) ([]*domain.Product, error) {
	cur, err := r.coll.Find(ctx, bson.M{"category": cat.String()}, options.Find())
	if err != nil {
		return nil, err
	}
	defer cur.Close(ctx)
	var out []*domain.Product
	for cur.Next(ctx) {
		var doc productDoc
		if err := cur.Decode(&doc); err != nil {
			return nil, err
		}
		out = append(out, docToProduct(&doc))
	}
	return out, cur.Err()
}

func (r *MongoProductRepository) Delete(ctx context.Context, id string) error {
	res, err := r.coll.DeleteOne(ctx, bson.M{"_id": id})
	if err != nil {
		return err
	}
	if res.DeletedCount == 0 {
		return domain.ErrProductNotFound
	}
	return nil
}

func docToProduct(d *productDoc) *domain.Product {
	cat, _ := domain.ParseCategory(d.Category)
	return &domain.Product{
		ID:          d.ID,
		Name:        d.Name,
		Description: d.Description,
		Category:    cat,
		Price:       d.Price,
		Image:       d.Image,
		Ingredients: d.Ingredients,
		CreatedAt:   d.CreatedAt,
		UpdatedAt:   d.UpdatedAt,
	}
}
