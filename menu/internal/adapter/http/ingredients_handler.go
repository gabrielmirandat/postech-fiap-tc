package http

import (
	"github.com/gabriel/menu/internal/application"
	"github.com/gabriel/menu/internal/domain"
	"net/http"
	"github.com/gin-gonic/gin"
)

type IngredientsHandler struct {
	svc *application.IngredientService
}

func NewIngredientsHandler(svc *application.IngredientService) *IngredientsHandler {
	return &IngredientsHandler{svc: svc}
}

func (h *IngredientsHandler) AddIngredient(c *gin.Context) {
	var req application.IngredientRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, application.ErrorResponse{Status: 400, Message: err.Error()})
		return
	}
	ing, err := h.svc.Create(c.Request.Context(), req)
	if err != nil {
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	c.JSON(http.StatusCreated, application.IngredientCreated{IngredientID: ing.ID})
}

func (h *IngredientsHandler) GetIngredientByID(c *gin.Context) {
	id := c.Param("ingredientId")
	ing, err := h.svc.GetByID(c.Request.Context(), id)
	if err != nil {
		if err == domain.ErrIngredientNotFound {
			c.JSON(http.StatusNotFound, application.ErrorResponse{Status: 404, Message: err.Error()})
			return
		}
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	c.JSON(http.StatusOK, application.IngredientResponse{
		ID:       ing.ID,
		Name:     ing.Name,
		Category: application.ProductCategoryDTO(ing.Category),
		Price:    ing.Price,
		Weight:   ing.Weight,
		IsExtra:  ing.IsExtra,
	})
}

func (h *IngredientsHandler) FindIngredientsByQuery(c *gin.Context) {
	catStr := c.Query("category")
	if catStr == "" {
		c.JSON(http.StatusOK, []application.IngredientResponse{})
		return
	}
	cat := application.ProductCategoryDTO(catStr)
	list, err := h.svc.SearchByCategory(c.Request.Context(), cat)
	if err != nil {
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	out := make([]application.IngredientResponse, 0, len(list))
	for _, ing := range list {
		out = append(out, application.IngredientResponse{
			ID:       ing.ID,
			Name:     ing.Name,
			Category: application.ProductCategoryDTO(ing.Category),
			Price:    ing.Price,
			Weight:   ing.Weight,
			IsExtra:  ing.IsExtra,
		})
	}
	c.JSON(http.StatusOK, out)
}
