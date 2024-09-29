import 'package:flutter/material.dart';

class LandingPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Bem-vindo ao Restaurante!'),
        actions: [
          IconButton(
            icon: Icon(Icons.login),
            onPressed: () {
              Navigator.pushNamed(context, '/login');
            },
          ),
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              child: Text('Menu'),
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
            ),
            ListTile(
              title: Text('Início'),
              onTap: () {
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: Text('Categorias'),
              onTap: () {
                Navigator.pushNamed(context, '/categories');
              },
            ),
          ],
        ),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Explore nosso menu!'),
            ElevatedButton(
              onPressed: () {
                Navigator.pushNamed(context, '/categories');
              },
              child: Text('Ver Categorias'),
            ),
          ],
        ),
      ),
    );
  }
}
