package repository

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"time"
)

type MongoIngredientRepository struct {
	coll *mongo.Collection
}

func NewMongoIngredientRepository(db *mongo.Database) *MongoIngredientRepository {
	return &MongoIngredientRepository{coll: db.Collection("ingredients")}
}

type ingredientDoc struct {
	ID        string    `bson:"_id"`
	Name      string    `bson:"name"`
	Category  string    `bson:"category"`
	Price     float64   `bson:"price"`
	Weight    float64   `bson:"weight"`
	IsExtra   bool      `bson:"isExtra"`
	CreatedAt time.Time `bson:"createdAt"`
	UpdatedAt time.Time `bson:"updatedAt"`
}

func (r *MongoIngredientRepository) Save(ctx context.Context, ing *domain.Ingredient) error {
	now := time.Now()
	ing.CreatedAt = now
	ing.UpdatedAt = now
	doc := ingredientDoc{
		ID:        ing.ID,
		Name:      ing.Name,
		Category:  ing.Category.String(),
		Price:     ing.Price,
		Weight:    ing.Weight,
		IsExtra:   ing.IsExtra,
		CreatedAt: now,
		UpdatedAt: now,
	}
	_, err := r.coll.InsertOne(ctx, doc)
	return err
}

func (r *MongoIngredientRepository) GetByID(ctx context.Context, id string) (*domain.Ingredient, error) {
	var doc ingredientDoc
	err := r.coll.FindOne(ctx, bson.M{"_id": id}).Decode(&doc)
	if err == mongo.ErrNoDocuments {
		return nil, domain.ErrIngredientNotFound
	}
	if err != nil {
		return nil, err
	}
	return docToIngredient(&doc), nil
}

func (r *MongoIngredientRepository) GetByIDs(ctx context.Context, ids []string) ([]*domain.Ingredient, error) {
	if len(ids) == 0 {
		return nil, nil
	}
	cur, err := r.coll.Find(ctx, bson.M{"_id": bson.M{"$in": ids}})
	if err != nil {
		return nil, err
	}
	defer cur.Close(ctx)
	var out []*domain.Ingredient
	for cur.Next(ctx) {
		var doc ingredientDoc
		if err := cur.Decode(&doc); err != nil {
			return nil, err
		}
		out = append(out, docToIngredient(&doc))
	}
	return out, cur.Err()
}

func (r *MongoIngredientRepository) SearchByCategory(ctx context.Context, cat domain.Category) ([]*domain.Ingredient, error) {
	cur, err := r.coll.Find(ctx, bson.M{"category": cat.String()}, options.Find())
	if err != nil {
		return nil, err
	}
	defer cur.Close(ctx)
	var out []*domain.Ingredient
	for cur.Next(ctx) {
		var doc ingredientDoc
		if err := cur.Decode(&doc); err != nil {
			return nil, err
		}
		out = append(out, docToIngredient(&doc))
	}
	return out, cur.Err()
}

func docToIngredient(d *ingredientDoc) *domain.Ingredient {
	cat, _ := domain.ParseCategory(d.Category)
	return &domain.Ingredient{
		ID:        d.ID,
		Name:      d.Name,
		Category:  cat,
		Price:     d.Price,
		Weight:    d.Weight,
		IsExtra:   d.IsExtra,
		CreatedAt: d.CreatedAt,
		UpdatedAt: d.UpdatedAt,
	}
}
