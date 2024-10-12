import 'package:flutter/cupertino.dart';

import '../product.dart';
import 'cart-item.dart';

class CartModel extends ChangeNotifier {
  final List<CartItemModel> _items = [];

  List<CartItemModel> get items => _items;

  void addItem(ProductModel product) {
    final index = _items.indexWhere((item) => item.product.id == product.id);
    if (index >= 0) {
      _items[index].quantity += 1;
    } else {
      _items.add(CartItemModel(product: product, quantity: 1));
    }
    notifyListeners();
  }

  void removeItem(ProductModel product) {
    _items.removeWhere((item) => item.product.id == product.id);
    notifyListeners();
  }

  void updateQuantity(ProductModel product, int quantity) {
    final index = _items.indexWhere((item) => item.product.id == product.id);
    if (index >= 0 && quantity > 0) {
      _items[index].quantity = quantity;
    } else if (index >= 0 && quantity == 0) {
      removeItem(product);
    }
    notifyListeners();
  }

  void clearCart() {
    _items.clear();
    notifyListeners();
  }

  double get totalPrice {
    return _items.fold(0, (total, current) => total + current.totalPrice);
  }
}