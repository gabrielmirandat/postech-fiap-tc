import 'package:provider/provider.dart';

import '../services/menu-service.dart';

final menuServiceProvider = Provider<MenuService>((ref) {
  return MenuService();
});

final menuProvider = FutureProvider<String>((ref) async {
  final menuService = ref.watch(menuServiceProvider);
  return await menuService.fetchMenu();
});
