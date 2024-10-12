import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:helloworld/models/ingredient.dart';
import '../models/product.dart';
import '../services/menu-service.dart';

final menuServiceProvider = Provider<MenuService>((ref) {
  return MenuService();
});

final productProvider = FutureProvider.family<List<ProductModel>, String>((ref, category) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchProducts(category);
});

final ingredientProvider = FutureProvider.family<List<IngredientModel>, String>((ref, category) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchExtras(category);
});