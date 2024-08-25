import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'pages/home.dart';
import 'pages/login.dart';
import 'pages/profile.dart'; // Adicione a importação do ProfileScreen
import 'provider/auth_provider.dart';
import 'theme.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => AuthProvider(),
      child: MaterialApp(
        title: 'Restaurant Orders App',
        theme: AppTheme.theme, // Usando o tema definido
        initialRoute: '/',
        routes: {
          '/': (context) => HomeScreen(),
          '/login': (context) => LoginScreen(),
          '/profile': (context) => ProfileScreen(),
          // Adicione a rota para o perfil
        },
      ),
    );
  }
}
