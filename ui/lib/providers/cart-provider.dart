import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/customer/cart.dart';

// Provider para gerenciar o estado do carrinho
final cartProvider = ChangeNotifierProvider<CartModel>((ref) {
  return CartModel();
});