import 'package:provider/provider.dart';

import '../services/permission-service.dart';

final permissionServiceProvider = Provider<PermissionService>((ref) {
  return PermissionService();
});

final loginProvider = FutureProvider.family<String, Map<String, String>>(
    (ref, credentials) async {
  final permissionService = ref.watch(permissionServiceProvider);
  final username = credentials['username']!;
  final password = credentials['password']!;
  return await permissionService.login(username, password);
});
