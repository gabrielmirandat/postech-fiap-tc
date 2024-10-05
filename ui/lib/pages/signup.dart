import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import '../services/permission-service.dart';

class SignUpScreen extends StatefulWidget {
  @override
  _SignUpScreenState createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final String _baseUrl = dotenv.env['BASE_URL'] ?? '';
  bool _isLoading = false;
  String? _errorMessage;

  Future<void> _signUp() async {
    setState(() {
      _isLoading = true; // Start loading
      _errorMessage = null; // Reset error message
    });

    try {
      final response = await PermissionService.signUp(
          _baseUrl, _usernameController.text, _passwordController.text);

      if (response.statusCode == 201) {
        print('Cadastro bem-sucedido');
        // Optionally navigate to another screen or show a success message
        // Navigator.pushReplacement(...);
      } else {
        print('Falha no cadastro');
        setState(() {
          _errorMessage = 'Falha no cadastro: ${response.body}';
        });
      }
    } catch (error) {
      print('Erro: $error');
      setState(() {
        _errorMessage = 'Erro ao cadastrar: $error';
      });
    } finally {
      setState(() {
        _isLoading = false; // Stop loading
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Sign Up'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            TextField(
              controller: _usernameController,
              decoration: InputDecoration(labelText: 'Username'),
            ),
            TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: InputDecoration(labelText: 'Password'),
            ),
            if (_errorMessage != null) // Show error message if exists
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: Text(
                  _errorMessage!,
                  style: TextStyle(color: Colors.red),
                ),
              ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _isLoading ? null : _signUp, // Disable button while loading
              child: _isLoading
                  ? CircularProgressIndicator()
                  : Text('Sign Up'),
            ),
          ],
        ),
      ),
    );
  }
}