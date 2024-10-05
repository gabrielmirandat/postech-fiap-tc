import 'dart:convert';
import 'dart:developer' as developer;
import 'package:http/http.dart' as http;

class PermissionService {
  static String get baseUrl => 'https://api.example.com';

  get isAuthenticated => null;

  get username => null;

  static Future<http.Response> signUp(String baseUrl, String username, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/signup'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'username': username,
          'password': password,
        }),
      );

      if (response.statusCode == 201) {
        developer.log('Sign-up successful: ${response.body}');
        return response; // Return the successful response
      } else {
        developer.log('Sign-up failed: ${response.statusCode} - ${response.body}');
        throw Exception('Failed to sign up: ${response.body}');
      }
    } catch (error) {
      developer.log('Error during sign-up: $error');
      throw Exception('Error during sign-up: $error');
    }
  }

  // Other methods like login and logout
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
        return response.body; // Return the response (e.g., a token)
      } else {
        developer.log('Login failed: ${response.statusCode} - ${response.body}');
        throw Exception('Failed to login: ${response.body}');
      }
    } catch (error) {
      developer.log('Error during login: $error');
      throw Exception('Error during login: $error');
    }
  }

  void logout() {
    // Implementation for logout
  }
}