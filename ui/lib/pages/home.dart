import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../provider/menu-provider.dart';
import '../provider/permission-provider.dart';
import '../models/menu.dart';

// Create the permissionServiceProvider
final permissionServiceProvider = ChangeNotifierProvider<PermissionProvider>((ref) {
  return PermissionProvider();
});

class HomeScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final permissionProvider = ref.watch(permissionServiceProvider);
    final menuAsyncValue = ref.watch(menuProvider); // Busca o menu

    return Scaffold(
      appBar: AppBar(
        title: Text(permissionProvider.isAuthenticated
            ? 'Welcome, ${permissionProvider.username}'
            : 'Restaurant Menu'),
        actions: [
          if (permissionProvider.isAuthenticated)
            IconButton(
              icon: Icon(Icons.exit_to_app),
              onPressed: () {
                permissionProvider.logout();
                Navigator.of(context).pushReplacementNamed('/'); // Retorna à tela inicial
              },
            ),
        ],
      ),
      body: Center(
        child: permissionProvider.isAuthenticated
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
            : Text('Please log in to access the menu.'),
      ),
    );
  }
}