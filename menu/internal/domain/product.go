package domain

import "time"

type Product struct {
	ID          string
	Name        string
	Description string
	Category    Category
	Price       float64
	Image       string
	Ingredients []string // ingredient IDs
	CreatedAt   time.Time
	UpdatedAt   time.Time
}
