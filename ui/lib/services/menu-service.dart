import 'dart:convert';

import '../models/menu.dart';
import 'package:http/http.dart' as http;

class MenuService {
  static String get baseUrl =>
      'https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io'; // You can replace this with your env variable

  Future<Menu> fetchProducts() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/products'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // Parse the response body and return a Menu object
        return Menu.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to fetch products: ${response.statusCode}');
      }
    } catch (e) {
      // Handle errors from the HTTP request
      throw Exception('Failed to fetch menu: $e');
    }
  }

  Future<Menu> fetchIngredients() async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/ingredients'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        // Parse the response body and return a Menu object
        return Menu.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to fetch ingredients: ${response.statusCode}');
      }
    } catch (e) {
      // Handle errors from the HTTP request
      throw Exception('Failed to fetch ingredients: $e');
    }
  }
}