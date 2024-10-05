import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../provider/menu-provider.dart';

class MenuScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final menuAsyncValue = ref.watch(menuProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text('Menu'),
      ),
      body: menuAsyncValue.when(
        data: (menu) {
          return ListView.builder(
            itemCount: menu.items.length,
            itemBuilder: (context, index) {
              final item = menu.items[index];
              return ListTile(
                title: Text(item.name),
                subtitle: Text('Price: R\$ ${item.price.toStringAsFixed(2)}'),
              );
            },
          );
        },
        loading: () => Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(child: Text('Error: $error')),
      ),
    );
  }
}