package http

import (
	"github.com/gabriel/menu/internal/application"
	"github.com/gabriel/menu/internal/domain"
	"net/http"
	"github.com/gin-gonic/gin"
)

type ProductsHandler struct {
	svc *application.ProductService
}

func NewProductsHandler(svc *application.ProductService) *ProductsHandler {
	return &ProductsHandler{svc: svc}
}

func (h *ProductsHandler) AddProduct(c *gin.Context) {
	var req application.ProductRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, application.ErrorResponse{Status: 400, Message: err.Error()})
		return
	}
	product, err := h.svc.Create(c.Request.Context(), req)
	if err != nil {
		if err == domain.ErrInvalidIngredients {
			c.JSON(http.StatusBadRequest, application.ErrorResponse{Status: 400, Message: err.Error()})
			return
		}
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	c.JSON(http.StatusCreated, application.ProductCreated{ProductID: product.ID})
}

func (h *ProductsHandler) GetProductByID(c *gin.Context) {
	id := c.Param("productId")
	resp, err := h.svc.GetResponseByID(c.Request.Context(), id)
	if err != nil {
		if err == domain.ErrProductNotFound {
			c.JSON(http.StatusNotFound, application.ErrorResponse{Status: 404, Message: err.Error()})
			return
		}
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	c.JSON(http.StatusOK, resp)
}

func (h *ProductsHandler) FindProductsByQuery(c *gin.Context) {
	catStr := c.Query("category")
	if catStr == "" {
		c.JSON(http.StatusOK, []application.ProductResponse{})
		return
	}
	cat := application.ProductCategoryDTO(catStr)
	list, err := h.svc.SearchByCategory(c.Request.Context(), cat)
	if err != nil {
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	if list == nil {
		list = []*application.ProductResponse{}
	}
	c.JSON(http.StatusOK, list)
}

func (h *ProductsHandler) DeleteProduct(c *gin.Context) {
	id := c.Param("productId")
	err := h.svc.Delete(c.Request.Context(), id)
	if err != nil {
		if err == domain.ErrProductNotFound {
			c.JSON(http.StatusNotFound, application.ErrorResponse{Status: 404, Message: err.Error()})
			return
		}
		c.JSON(http.StatusInternalServerError, application.ErrorResponse{Status: 500, Message: err.Error()})
		return
	}
	c.Status(http.StatusNoContent)
}
