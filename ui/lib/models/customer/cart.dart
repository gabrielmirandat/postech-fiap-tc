import 'package:flutter/cupertino.dart';

import '../product.dart';
import 'cart-item.dart';

class CartModel extends ChangeNotifier { // Extende ChangeNotifier
  final List<CartItemModel> items = [];

  // Adiciona item ao carrinho
  void addItem(ProductModel product) {
    // Verifica se o item já está no carrinho
    final index = items.indexWhere((item) => item.product.id == product.id);
    if (index >= 0) {
      items[index].quantity += 1; // Aumenta a quantidade
    } else {
      items.add(CartItemModel(product: product));
    }
    notifyListeners(); // Notifica ouvintes sobre a mudança
  }

  // Remove um item
  void removeItem(ProductModel product) {
    items.removeWhere((item) => item.product.id == product.id);
    notifyListeners(); // Notifica ouvintes sobre a mudança
  }

  // Limpa o carrinho
  void clearCart() {
    items.clear();
    notifyListeners(); // Notifica ouvintes sobre a mudança
  }

  // Calcula o preço total do carrinho
  double get totalPrice => items.fold(0, (total, item) => total + item.totalPrice);
}