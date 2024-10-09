import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:helloworld/models/ingredient.dart';
import '../models/product.dart';
import '../services/menu-service.dart';

final menuServiceProvider = Provider<MenuService>((ref) {
  return MenuService();
});

final productProvider = FutureProvider<List<Product>>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchProducts();
});

final ingredientProvider = FutureProvider<List<Ingredient>>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchIngredients();
});