import 'dart:convert';

import '../models/menu.dart';
import 'package:http/http.dart' as http;

class MenuService {
  static String get baseUrl =>
      'https://api.example.com'; // You can replace this with your env variable

  Future<Menu> fetchMenu() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/menu'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // Parse the response body and return a Menu object
        return Menu.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to fetch menu: ${response.statusCode}');
      }
    } catch (e) {
      // Handle errors from the HTTP request
      throw Exception('Failed to fetch menu: $e');
    }
  }
}