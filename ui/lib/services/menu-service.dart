import 'dart:convert';

import '../models/menu.dart';
import 'package:http/http.dart' as http;

class MenuService {
  static String get baseUrl =>
      'https://0e6b3bb3-58a1-4d45-927c-56712eb2740a.mock.pstmn.io/'; // You can replace this with your env variable

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