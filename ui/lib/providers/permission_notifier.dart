import 'package:flutter/material.dart';
import '../services/permission-service.dart';

class PermissionProvider extends ChangeNotifier {
  final PermissionService _permissionService = PermissionService();

  bool _isAuthenticated = false;

  bool get isAuthenticated => _isAuthenticated;

  Future<void> login(String username, String password) async {
    final success = await _permissionService.login(username, password);
    if (success) {
      _isAuthenticated = true;
      notifyListeners();
    }
  }

  void logout() {
    _permissionService.logout();
    _isAuthenticated = false;
    notifyListeners();
  }
}