import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../models/menu.dart';
import '../services/menu-service.dart';

// Provider for MenuService
final menuServiceProvider = Provider<MenuService>((ref) {
  return MenuService();
});

// FutureProvider for fetching the menu
final menuProvider = FutureProvider<Menu>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchMenu();
});