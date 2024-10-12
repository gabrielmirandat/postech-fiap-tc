import 'dart:convert';
import 'package:http/http.dart' as http;

class PermissionService {
  String? _token;

  Future<bool> login(String username, String password) async {

    final response = await http.post(
      Uri.parse('https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io/users/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'username': username,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      _token = jsonDecode(response.body)['access_token'];
      return true;
    }
    return false;
  }

  bool isLoggedIn() {
    return _token != null;
  }

  void logout() {
    _token = null;
  }
}