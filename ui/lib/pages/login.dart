import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../provider/permission-provider.dart';

// Criação do permissionServiceProvider
final permissionServiceProvider = ChangeNotifierProvider<PermissionProvider>((ref) {
  return PermissionProvider();
});

class LoginScreen extends ConsumerWidget {
  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authProvider = ref.watch(permissionServiceProvider);

    // Redireciona após autenticação
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (authProvider.isAuthenticated) {
        Navigator.of(context).pushReplacementNamed('/');
      }
    });

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
                ref.read(permissionServiceProvider).login(
                  usernameController.text,
                  passwordController.text,
                );
              },
              child: Text('Login'),
            ),
            SizedBox(height: 16),
            if (authProvider.loading)
              CircularProgressIndicator()
            else if (authProvider.error != null)
              Text('Error: ${authProvider.error}')
            else if (authProvider.isAuthenticated)
                Text('Login Success! Welcome ${authProvider.username}'),
          ],
        ),
      ),
    );
  }
}