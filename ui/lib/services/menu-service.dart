import 'package:http/http.dart' as http;

class MenuService {
  static String get baseUrl =>
      'https://api.example.com'; // Use o valor de env para Web

  Future<String> fetchMenu() async {
    final response = await http.get(
      Uri.parse('$baseUrl/menu'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Failed to fetch menu');
    }
  }
}
