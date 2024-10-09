import 'dart:convert';

import 'package:http/http.dart' as http;

import '../models/product.dart';
import '../models/ingredient.dart';

class MenuService {

  Future<List<Product>> fetchProducts() async {
    try {
      final response = await http.get(
        Uri.parse('https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io/products'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        return Product.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to fetch products: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Failed to fetch menu: $e');
    }
  }

  Future<List<Ingredient>> fetchIngredients() async {
    try {
      final response = await http.get(
        Uri.parse('https://03277fc5-a4e8-4351-b0f5-349dd0511f19.mock.pstmn.io/ingredients'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
      );

      if (response.statusCode == 200) {
        return Ingredient.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to fetch ingredients: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Failed to fetch ingredients: $e');
    }
  }
}