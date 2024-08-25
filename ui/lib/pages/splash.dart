import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/auth_provider.dart';

class SplashScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    Future.delayed(Duration(seconds: 2), () {
      final isAuthenticated =
          Provider.of<AuthProvider>(context, listen: false).isAuthenticated;
      Navigator.of(context)
          .pushReplacementNamed(isAuthenticated ? '/home' : '/login');
    });

    return Scaffold(
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }
}
