import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/permission_state_provider.dart';
import 'menu.dart';

class HomeScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final permissionState = ref.watch(permissionProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(permissionState.isAuthenticated
            ? 'Welcome, ${permissionState.username}'
            : 'Restaurant Menu'),
        actions: [
          if (permissionState.isAuthenticated)
            IconButton(
              icon: Icon(Icons.exit_to_app),
              onPressed: () {
                permissionState.logout();
                Navigator.of(context).pushReplacementNamed('/'); // Retorna à tela inicial
              },
            ),
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
              child: Text(permissionState.isAuthenticated
                  ? 'Hello, ${permissionState.username}'
                  : 'Welcome!'),
            ),
            ListTile(
              leading: Icon(Icons.login),
              title: Text('Login'),
              onTap: () {
                if (!permissionState.isAuthenticated) {
                  Navigator.of(context).pushNamed('/login'); // Vai para a tela de login
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('You are already logged in')));
                }
              },
            ),
            if (permissionState.isAuthenticated)
              ListTile(
                leading: Icon(Icons.exit_to_app),
                title: Text('Logout'),
                onTap: () {
                  permissionState.logout();
                  Navigator.of(context).pushReplacementNamed('/'); // Retorna à tela inicial
                },
              ),
          ],
        ),
      ),
      body: Center(
        child: permissionState.isAuthenticated
            ? Menu() // Use o novo widget Menu aqui
            : Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('Please log in to access the menu.'),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pushNamed('/login'); // Navega para a tela de login
              },
              child: Text('Login'),
            ),
          ],
        ),
      ),
    );
  }
}