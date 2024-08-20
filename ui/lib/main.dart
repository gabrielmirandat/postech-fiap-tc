import 'dart:math';

import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Random Word App',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepOrange),
        textTheme: ThemeData.light().textTheme.copyWith(
              headlineMedium:
                  TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final List<String> _words = [
    'Flutter',
    'Dart',
    'Development',
    'Application',
    'Random',
    'Word',
    'Generator'
  ];

  String _randomWord = '';

  @override
  void initState() {
    super.initState();
    _generateRandomWord();
  }

  void _generateRandomWord() {
    final random = Random();
    setState(() {
      _randomWord = _words[random.nextInt(_words.length)];
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Random Word Generator'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('A random word:'),
            Text(
              _randomWord,
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _generateRandomWord,
              child: Text('Generate New Word'),
            ),
          ],
        ),
      ),
    );
  }
}
