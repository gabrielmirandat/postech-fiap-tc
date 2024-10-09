import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/product.dart';
import '../services/menu-service.dart';

final menuServiceProvider = Provider<MenuService>((ref) {
  return MenuService();
});

final menuProvider = FutureProvider<List<Product>>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchProducts();
});