import 'package:flutter_dotenv/flutter_dotenv.dart';

class EnvConfig {
  static String get baseUrl {
    try {
      return dotenv.get('BASE_URL', fallback: 'http://127.0.0.1:8000');
    } catch (e) {
      print('Error getting BASE_URL: $e');
      return 'http://127.0.0.1:8000';
    }
  }

  static String get auth0Audience {
    try {
      return dotenv.get('AUTH0_AUDIENCE', fallback: '');
    } catch (e) {
      print('Error getting AUTH0_AUDIENCE: $e');
      return '';
    }
  }

  static String get auth0ClientId {
    try {
      return dotenv.get('AUTH0_CLIENT_ID', fallback: '');
    } catch (e) {
      print('Error getting AUTH0_CLIENT_ID: $e');
      return '';
    }
  }

  static String get auth0Domain {
    try {
      return dotenv.get('AUTH0_DOMAIN', fallback: '');
    } catch (e) {
      print('Error getting AUTH0_DOMAIN: $e');
      return '';
    }
  }
}
