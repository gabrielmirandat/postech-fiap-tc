package application

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
)

type ProductRepository interface {
	Save(ctx context.Context, p *domain.Product) error
	GetByID(ctx context.Context, id string) (*domain.Product, error)
	SearchByCategory(ctx context.Context, cat domain.Category) ([]*domain.Product, error)
	Delete(ctx context.Context, id string) error
}

type IngredientRepository interface {
	Save(ctx context.Context, ing *domain.Ingredient) error
	GetByID(ctx context.Context, id string) (*domain.Ingredient, error)
	GetByIDs(ctx context.Context, ids []string) ([]*domain.Ingredient, error)
	SearchByCategory(ctx context.Context, cat domain.Category) ([]*domain.Ingredient, error)
}

type ProductPublisher interface {
	ProductCreated(ctx context.Context, p *domain.Product) error
}

type IngredientPublisher interface {
	IngredientCreated(ctx context.Context, ing *domain.Ingredient) error
}
