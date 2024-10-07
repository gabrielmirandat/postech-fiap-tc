import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../provider/menu-provider.dart';
import '../provider/permission-provider.dart';
import '../models/menu.dart';

final permissionServiceProvider = ChangeNotifierProvider<PermissionProvider>((ref) {
  return PermissionProvider();
});

class HomeScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authProvider = ref.watch(permissionServiceProvider);
    final menuAsyncValue = ref.watch(menuProvider); // Busca o menu

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
              child: Text(authProvider.isAuthenticated
                  ? 'Hello, ${authProvider.username}'
                  : 'Welcome!'),
            ),
            ListTile(
              leading: Icon(Icons.login),
              title: Text('Login'),
              onTap: () {
                if (!authProvider.isAuthenticated) {
                  Navigator.of(context).pushNamed('/login'); // Vai para a tela de login
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('You are already logged in')));
                }
              },
            ),
            if (authProvider.isAuthenticated)
              ListTile(
                leading: Icon(Icons.exit_to_app),
                title: Text('Logout'),
                onTap: () {
                  authProvider.logout();
                  Navigator.of(context).pushReplacementNamed('/'); // Retorna à tela inicial
                },
              ),
          ],
        ),
      ),
      body: Center(
        child: authProvider.isAuthenticated
            ? menuAsyncValue.when(
          data: (menu) {
            return ListView.builder(
              itemCount: menu.items.length,
              itemBuilder: (context, index) {
                final item = menu.items[index];
                return ListTile(
                  title: Text(item.name),
                  subtitle: Text('${item.price} USD'),
                );
              },
            );
          },
          loading: () => CircularProgressIndicator(),
          error: (error, stack) => Text('Error: $error'),
        )
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