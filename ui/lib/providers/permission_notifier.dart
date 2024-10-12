import 'package:flutter/material.dart';
import '../services/permission-service.dart';

class PermissionProvider extends ChangeNotifier {
  final PermissionService _permissionService = PermissionService();
  bool _isAuthenticated = false;
  String _username = '';

  bool get isAuthenticated => _isAuthenticated;
  String get username => _username;

  Future<void> login(String username, String password) async {
    final success = await _permissionService.login(username, password);
    if (success) {
      _isAuthenticated = true;
      _username = username;
      notifyListeners();
    }
  }

  void logout() {
    _permissionService.logout();
    _isAuthenticated = false;
    _username = '';
    notifyListeners();
  }
}