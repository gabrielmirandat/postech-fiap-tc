package domain

import "fmt"

type Category string

const (
	CategoryBurger        Category = "burger"
	CategoryAccompaniment Category = "accompaniment"
	CategoryDessert       Category = "dessert"
	CategoryDrink         Category = "drink"
)

var validCategories = map[Category]bool{
	CategoryBurger: true, CategoryAccompaniment: true,
	CategoryDessert: true, CategoryDrink: true,
}

func ParseCategory(s string) (Category, error) {
	c := Category(s)
	if !validCategories[c] {
		return "", fmt.Errorf("invalid category: %s", s)
	}
	return c, nil
}

func (c Category) String() string { return string(c) }
