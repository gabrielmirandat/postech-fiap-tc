package domain

import "testing"

func TestParseCategory(t *testing.T) {
	tests := []struct {
		s    string
		want Category
		ok   bool
	}{
		{"burger", CategoryBurger, true},
		{"accompaniment", CategoryAccompaniment, true},
		{"dessert", CategoryDessert, true},
		{"drink", CategoryDrink, true},
		{"invalid", "", false},
		{"", "", false},
	}
	for _, tt := range tests {
		got, err := ParseCategory(tt.s)
		if tt.ok && err != nil {
			t.Errorf("ParseCategory(%q) err = %v", tt.s, err)
			continue
		}
		if !tt.ok && err == nil {
			t.Errorf("ParseCategory(%q) wanted error", tt.s)
			continue
		}
		if tt.ok && got != tt.want {
			t.Errorf("ParseCategory(%q) = %v, want %v", tt.s, got, tt.want)
		}
	}
}

func TestCategory_String(t *testing.T) {
	if CategoryBurger.String() != "burger" {
		t.Errorf("CategoryBurger.String() = %q", CategoryBurger.String())
	}
}
