import 'product.dart';

class CartItem {
  final Product product;
  final int quantity;

  CartItem(this.product, this.quantity);

  double get totalPrice => product.price * quantity;
}