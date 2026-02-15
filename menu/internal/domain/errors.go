package domain

import "errors"

var (
	ErrProductNotFound     = errors.New("product not found")
	ErrIngredientNotFound   = errors.New("ingredient not found")
	ErrInvalidIngredients  = errors.New("some of the ingredients are invalid")
)
