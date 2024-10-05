import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:helloworld/provider/login-provider.dart';

import '../models/login-state.dart';

// Define the loginProvider
final loginProvider = StateNotifierProvider<LoginProvider, LoginState>((ref) {
  return LoginProvider();
});

class LoginScreen extends ConsumerWidget {
  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final loginState = ref.watch(loginProvider);

    return Scaffold(
      appBar: AppBar(title: Text('Login')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: usernameController,
              decoration: InputDecoration(labelText: 'Username'),
            ),
            TextField(
              controller: passwordController,
              decoration: InputDecoration(labelText: 'Password'),
              obscureText: true,
            ),
            ElevatedButton(
              onPressed: () {
                ref.read(loginProvider.notifier).login(
                  usernameController.text,
                  passwordController.text,
                );
              },
              child: Text('Login'),
            ),
            SizedBox(height: 16),
            // Check login state
            if (loginState.loading)
              CircularProgressIndicator()
            else if (loginState.error != null)
              Text('Error: ${loginState.error}')
            else
              Text('Login Success!'),
          ],
        ),
      ),
    );
  }
}