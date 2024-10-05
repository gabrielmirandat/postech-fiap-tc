import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../provider/permission_provider.dart'; // Ensure this is the correct path

// Create the permissionServiceProvider
final permissionServiceProvider = ChangeNotifierProvider<PermissionProvider>((ref) {
  return PermissionProvider();
});

class HomeScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // Access the PermissionProvider using ref.watch
    final authProvider = ref.watch(permissionServiceProvider);

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
                Navigator.of(context).pushReplacementNamed('/'); // Return to home
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
                  Navigator.of(context).pushNamed('/login'); // Navigate to login
                },
              ),
            if (authProvider.isAuthenticated)
              ListTile(
                title: Text('Place an Identified Order'),
                onTap: () {
                  // Navigate to the order page
                  Navigator.of(context).pushNamed('/order');
                },
              ),
          ],
        ),
      ),
      body: Center(
        child: authProvider.isAuthenticated
            ? Text('Welcome to Our Restaurant!')
            : Text('Please log in to access the menu.'),
      ),
    );
  }
}