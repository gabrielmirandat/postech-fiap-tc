package application

import "github.com/gabriel/menu/internal/domain"

// API request/response DTOs (match OAS menu-api.yaml)

type ProductCategoryDTO string

const (
	CategoryDTOBurger        ProductCategoryDTO = "burger"
	CategoryDTOAccompaniment ProductCategoryDTO = "accompaniment"
	CategoryDTODessert       ProductCategoryDTO = "dessert"
	CategoryDTODrink         ProductCategoryDTO = "drink"
)

type ProductIngredientRequest struct {
	IngredientID string `json:"ingredientId" binding:"required"`
	Quantity     int    `json:"quantity" binding:"required,min=1,max=4"`
}

type ProductRequest struct {
	Name        string                   `json:"name" binding:"required"`
	Description string                   `json:"description" binding:"required"`
	Category    ProductCategoryDTO       `json:"category" binding:"required"`
	Price       float64                  `json:"price" binding:"required"`
	Image       string                   `json:"image" binding:"required"`
	Ingredients []ProductIngredientRequest `json:"ingredients" binding:"required,min=1,max=6"`
}

type ProductCreated struct {
	ProductID string `json:"productId"`
}

type IngredientRequest struct {
	Name    string             `json:"name" binding:"required"`
	Category ProductCategoryDTO `json:"category" binding:"required"`
	Price   float64            `json:"price" binding:"required,min=0.1"`
	Weight  float64            `json:"weight" binding:"required,min=0.1"`
	IsExtra bool               `json:"isExtra" binding:"required"`
}

type IngredientCreated struct {
	IngredientID string `json:"ingredientId"`
}

type IngredientResponse struct {
	ID       string             `json:"id"`
	Name     string             `json:"name"`
	Category ProductCategoryDTO  `json:"category"`
	Price    float64            `json:"price"`
	Weight   float64            `json:"weight"`
	IsExtra  bool                `json:"isExtra"`
}

type ProductResponse struct {
	ID          string               `json:"id"`
	Name        string               `json:"name"`
	Description string               `json:"description"`
	Category    ProductCategoryDTO    `json:"category"`
	Price       float64              `json:"price"`
	Image       string               `json:"image"`
	Ingredients []IngredientResponse `json:"ingredients"`
}

type ErrorResponse struct {
	Status  int    `json:"status"`
	Message string `json:"message"`
	Code    string `json:"code,omitempty"`
}

func categoryToDTO(c domain.Category) ProductCategoryDTO {
	return ProductCategoryDTO(c)
}
