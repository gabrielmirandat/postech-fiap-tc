import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../services/permission-service.dart';

class PermissionProvider extends ChangeNotifier {
  bool _isAuthenticated = false;
  String _username = '';
  String? _error;
  bool _loading = false;

  bool get isAuthenticated => _isAuthenticated;
  String get username => _username;
  bool get loading => _loading;
  String? get error => _error;

  Future<void> login(String username, String password) async {
    _loading = true;
    notifyListeners();

    try {
      final token = await PermissionService.login(username, password);
      _isAuthenticated = true;
      _username = username; // Ou extraído da resposta, dependendo da API
      _error = null;
    } catch (e) {
      _isAuthenticated = false;
      _username = '';
      _error = e.toString();
    } finally {
      _loading = false;
      notifyListeners();
    }
  }

  void logout() {
    _isAuthenticated = false;
    _username = '';
    notifyListeners();
    // Chamar PermissionService.logout() se necessário
  }
}