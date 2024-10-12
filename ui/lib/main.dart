import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'pages/home.dart';
import 'pages/login.dart';
import 'theme.dart';

void main() {
  runApp(ProviderScope(
      child: MyApp()
  )); // Use Riverpod's ProviderScope
}

class MyApp extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // Remove ChangeNotifierProvider as we are using Riverpod instead
    return MaterialApp(
      title: 'Restaurant Orders App',
      theme: AppTheme.theme, // Apply the defined theme
      initialRoute: '/',
      routes: {
        '/': (context) => HomeScreen(),
        '/login': (context) => LoginScreen()
      },
    );
  }
}