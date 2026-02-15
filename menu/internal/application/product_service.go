package application

import (
	"context"
	"github.com/gabriel/menu/internal/domain"
	"github.com/google/uuid"
)

type ProductService struct {
	productRepo    ProductRepository
	ingredientSvc  *IngredientService
	productPublish ProductPublisher
}

func NewProductService(
	productRepo ProductRepository,
	ingredientSvc *IngredientService,
	productPublish ProductPublisher,
) *ProductService {
	return &ProductService{
		productRepo:    productRepo,
		ingredientSvc:  ingredientSvc,
		productPublish: productPublish,
	}
}

func (s *ProductService) Create(ctx context.Context, req ProductRequest) (*domain.Product, error) {
	cat, err := domain.ParseCategory(string(req.Category))
	if err != nil {
		return nil, err
	}
	ingredientIDs := make([]string, 0, len(req.Ingredients))
	for _, i := range req.Ingredients {
		ingredientIDs = append(ingredientIDs, i.IngredientID)
	}
	// Validate all ingredient IDs exist and belong to same category
	existing, err := s.ingredientSvc.GetByIDs(ctx, ingredientIDs)
	if err != nil || len(existing) != len(ingredientIDs) {
		return nil, domain.ErrInvalidIngredients
	}
	for _, e := range existing {
		if e.Category != cat {
			return nil, domain.ErrInvalidIngredients
		}
	}
	product := &domain.Product{
		ID:          newProductID(),
		Name:        req.Name,
		Description: req.Description,
		Category:    cat,
		Price:       req.Price,
		Image:       req.Image,
		Ingredients: ingredientIDs,
	}
	if err := s.productRepo.Save(ctx, product); err != nil {
		return nil, err
	}
	_ = s.productPublish.ProductCreated(ctx, product)
	return product, nil
}

func (s *ProductService) GetByID(ctx context.Context, id string) (*domain.Product, error) {
	return s.productRepo.GetByID(ctx, id)
}

func (s *ProductService) GetResponseByID(ctx context.Context, id string) (*ProductResponse, error) {
	product, err := s.productRepo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}
	ingredients, err := s.ingredientSvc.GetByIDs(ctx, product.Ingredients)
	if err != nil {
		return nil, err
	}
	return toProductResponse(product, ingredients), nil
}

func (s *ProductService) SearchByCategory(ctx context.Context, cat ProductCategoryDTO) ([]*ProductResponse, error) {
	c, err := domain.ParseCategory(string(cat))
	if err != nil {
		return nil, err
	}
	products, err := s.productRepo.SearchByCategory(ctx, c)
	if err != nil {
		return nil, err
	}
	out := make([]*ProductResponse, 0, len(products))
	for _, p := range products {
		resp, err := s.GetResponseByID(ctx, p.ID)
		if err != nil {
			continue
		}
		out = append(out, resp)
	}
	return out, nil
}

func (s *ProductService) Delete(ctx context.Context, id string) error {
	return s.productRepo.Delete(ctx, id)
}

func toProductResponse(p *domain.Product, ingredients []*domain.Ingredient) *ProductResponse {
	ingResp := make([]IngredientResponse, 0, len(ingredients))
	for _, i := range ingredients {
		ingResp = append(ingResp, IngredientResponse{
			ID:       i.ID,
			Name:     i.Name,
			Category: categoryToDTO(i.Category),
			Price:    i.Price,
			Weight:   i.Weight,
			IsExtra:  i.IsExtra,
		})
	}
	return &ProductResponse{
		ID:          p.ID,
		Name:        p.Name,
		Description: p.Description,
		Category:    categoryToDTO(p.Category),
		Price:       p.Price,
		Image:       p.Image,
		Ingredients: ingResp,
	}
}

func newProductID() string {
	return uuid.New().String()[:8] + "-prdc-" + uuid.New().String()[:8]
}
