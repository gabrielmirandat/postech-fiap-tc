package messaging

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
)

type NoopProductPublisher struct{}
type NoopIngredientPublisher struct{}

func (NoopProductPublisher) ProductCreated(ctx context.Context, p *domain.Product) error {
	return nil
}

func (NoopIngredientPublisher) IngredientCreated(ctx context.Context, ing *domain.Ingredient) error {
	return nil
}
