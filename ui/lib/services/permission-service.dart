import 'dart:convert';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;

class PermissionService {
  static String get baseUrl => 'https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io/users';

  static Future<String> login(String username, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/login'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'username': username,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        developer.log('Login successful: ${response.body}');
        return response.body; // Retorna o token ou as informações necessárias
      } else {
        developer.log('Login failed: ${response.statusCode} - ${response.body}');
        throw Exception('Failed to login: ${response.body}');
      }
    } catch (error) {
      developer.log('Error during login: $error');
      throw Exception('Error during login: $error');
    }
  }

  static Future<void> logout() async {
    // Implementar chamada para logout, se necessário
  }
}