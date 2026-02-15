package domain

import "time"

type Ingredient struct {
	ID        string
	Name      string
	Category  Category
	Price     float64
	Weight    float64
	IsExtra   bool
	CreatedAt time.Time
	UpdatedAt time.Time
}
