import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/permission_provider.dart';

class LoginScreen extends ConsumerWidget {
  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: [
        TextField(
            controller: usernameController,
            decoration: InputDecoration(labelText: 'Username')),
        TextField(
            controller: passwordController,
            decoration: InputDecoration(labelText: 'Password')),
        ElevatedButton(
          onPressed: () {
            final credentials = {
              'username': usernameController.text,
              'password': passwordController.text,
            };
            ref.read(loginProvider(credentials));
          },
          child: Text('Login'),
        ),
        Consumer(
          builder: (context, ref, child) {
            final loginState = ref?.watch(loginProvider({
              'username': usernameController.text,
              'password': passwordController.text
            }));

            return loginState.when(
              data: (data) => Text('Login Success!'),
              loading: () => CircularProgressIndicator(),
              error: (error, stack) => Text('Error: $error'),
            );
          },
        ),
      ],
    );
  }
}
