import 'dart:convert';

import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:http/http.dart' as http;

import '../models/login-state.dart';

class LoginProvider extends StateNotifier<LoginState> {
  LoginProvider() : super(LoginState());

  Future<void> login(String username, String password) async {
    state = LoginState(loading: true); // Set loading to true
    try {
      final response = await http.post(
        Uri.parse('https://api.example.com/login'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(<String, String>{
          'username': username,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        // Handle successful login (store token, navigate, etc.)
        state = LoginState(); // Reset state on success
      } else {
        state = LoginState(loading: false, error: 'Login failed');
      }
    } catch (e) {
      state = LoginState(loading: false, error: e.toString());
    }
  }
}