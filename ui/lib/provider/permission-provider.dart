import 'package:flutter/cupertino.dart';

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
    notifyListeners(); // Notificar que o carregamento começou

    try {
      final token = await PermissionService.login(username, password);
      _isAuthenticated = true;
      _username = username;
      _error = null;
    } catch (e) {
      _isAuthenticated = false;
      _username = '';
      _error = e.toString();
    } finally {
      _loading = false;
      notifyListeners(); // Notificar que o estado mudou (sucesso ou falha)
    }
  }

  void logout() {
    _isAuthenticated = false;
    _username = '';
    notifyListeners(); // Notificar que o usuário foi deslogado
  }
}