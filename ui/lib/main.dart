import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';

import 'pages/home.dart';
import 'pages/login.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Carregar o arquivo de ambiente
  try {
    await dotenv.load(
        fileName: '.env.devl'); // Atualize aqui para o nome correto do arquivo
    print('Loaded environment file: .env.devl');
  } catch (e) {
    print('Error loading environment file: $e');
  }

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'My App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: LoginScreen(), // Tela inicial do app
      routes: {
        '/home': (context) => HomeScreen(), // Rota para a tela inicial
      },
    );
  }
}
