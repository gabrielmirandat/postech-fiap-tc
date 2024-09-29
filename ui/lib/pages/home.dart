import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/permission_provider.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<PermissionProvider>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text(authProvider.isAuthenticated
            ? 'Welcome, ${authProvider.username}'
            : 'Restaurant Menu'),
        actions: [
          if (authProvider.isAuthenticated)
            IconButton(
              icon: Icon(Icons.exit_to_app),
              onPressed: () {
                authProvider.logout();
                Navigator.of(context).pushReplacementNamed('/');
              },
            ),
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blueGrey,
              ),
              child: Text(
                'Restaurant App',
                style: TextStyle(color: Colors.white, fontSize: 24),
              ),
            ),
            if (!authProvider.isAuthenticated)
              ListTile(
                title: Text('Login'),
                onTap: () {
                  Navigator.of(context).pushNamed('/login');
                },
              ),
            if (authProvider.isAuthenticated)
              ListTile(
                title: Text('Place an Identified Order'),
                onTap: () {
                  // Navigate to the order page or similar
                },
              ),
          ],
        ),
      ),
      body: Center(
        child: Text('Welcome to Our Restaurant!'),
      ),
    );
  }
}
