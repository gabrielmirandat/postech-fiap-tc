import 'dart:convert';
import 'dart:developer' as developer;

import 'package:http/http.dart' as http;

class PermissionService {
  static String get baseUrl =>
      'https://api.example.com'; // Use o valor de env para Web

  static Future<void> login(String username, String password) async {
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
        developer.log('Login bem-sucedido: ${response.body}');
      } else {
        developer
            .log('Falha no login: ${response.statusCode} - ${response.body}');
      }
    } catch (error) {
      developer.log('Erro ao tentar fazer login: $error');
    }
  }

  static Future<http.Response> signUp(
      String baseUrl, String username, String password) {
    return http.post(
      Uri.parse('$baseUrl/signup'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'username': username,
        'password': password,
      }),
    );
  }
}
