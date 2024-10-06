import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/permission-provider.dart';

class SplashScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // Schedule a callback to run after the current frame
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Future.delayed(Duration(seconds: 2), () {
        final isAuthenticated =
            Provider.of<PermissionProvider>(context, listen: false)
                .isAuthenticated;
        Navigator.of(context)
            .pushReplacementNamed(isAuthenticated ? '/home' : '/login');
      });
    });

    return Scaffold(
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }
}