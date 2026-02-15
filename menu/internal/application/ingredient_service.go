package application

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
	"github.com/google/uuid"
)

type IngredientService struct {
	repo      IngredientRepository
	publisher IngredientPublisher
}

func NewIngredientService(repo IngredientRepository, publisher IngredientPublisher) *IngredientService {
	return &IngredientService{repo: repo, publisher: publisher}
}

func (s *IngredientService) Create(ctx context.Context, req IngredientRequest) (*domain.Ingredient, error) {
	cat, err := domain.ParseCategory(string(req.Category))
	if err != nil {
		return nil, err
	}
	ing := &domain.Ingredient{
		ID:       newIngredientID(),
		Name:     req.Name,
		Category: cat,
		Price:    req.Price,
		Weight:   req.Weight,
		IsExtra:  req.IsExtra,
	}
	if err := s.repo.Save(ctx, ing); err != nil {
		return nil, err
	}
	_ = s.publisher.IngredientCreated(ctx, ing)
	return ing, nil
}

func (s *IngredientService) GetByID(ctx context.Context, id string) (*domain.Ingredient, error) {
	return s.repo.GetByID(ctx, id)
}

func (s *IngredientService) GetByIDs(ctx context.Context, ids []string) ([]*domain.Ingredient, error) {
	return s.repo.GetByIDs(ctx, ids)
}

func (s *IngredientService) SearchByCategory(ctx context.Context, cat ProductCategoryDTO) ([]*domain.Ingredient, error) {
	c, err := domain.ParseCategory(string(cat))
	if err != nil {
		return nil, err
	}
	return s.repo.SearchByCategory(ctx, c)
}

func newIngredientID() string {
	return uuid.New().String()[:8] + "-ingr-" + uuid.New().String()[:8]
}
