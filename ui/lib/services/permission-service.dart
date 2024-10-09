import 'dart:convert';
import 'dart:developer' as developer;
import 'package:helloworld/models/login-state.dart';
import 'package:http/http.dart' as http;

class PermissionService {

  // static Future<String> login(String username, String password) async {
  //   try {
  //     final response = await http.post(
  //       Uri.parse('https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io/users/login'),
  //       headers: <String, String>{
  //         'Content-Type': 'application/json; charset=UTF-8',
  //       },
  //       body: jsonEncode(<String, String>{
  //         'username': username,
  //         'password': password,
  //       }),
  //     );
  //
  //     if (response.statusCode == 200) {
  //       developer.log('Login successful: ${response.body}');
  //       return response.body; // Retorna o token ou as informações necessárias
  //     } else {
  //       developer.log('Login failed: ${response.statusCode} - ${response.body}');
  //       throw Exception('Failed to login: ${response.body}');
  //     }
  //   } catch (error) {
  //     developer.log('Error during login: $error');
  //     throw Exception('Error during login: $error');
  //   }
  // }
  //
  // static Future<void> logout() async {
  //   // Implementar chamada para logout, se necessário
  // }

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