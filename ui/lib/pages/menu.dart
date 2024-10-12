import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/menu-provider.dart';

class Menu extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final productState = ref.watch(productProvider); // Busca o menu

    return productState.when(
      data: (productList) {
        return ListView.builder(
          itemCount: productList.length,
          itemBuilder: (context, index) {
            final item = productList[index];
            return Card(
              margin: EdgeInsets.symmetric(vertical: 10, horizontal: 15),
              child: ListTile(
                leading: Image.asset(
                  'assets/images/${item.category}/${item.image}',
                  fit: BoxFit.cover,
                  width: 50,
                ),
                title: Text(item.name, style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                subtitle: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(item.description),
                    Text('${item.price.toStringAsFixed(2)} USD',
                        style: TextStyle(color: Colors.green)),
                  ],
                ),
                trailing: ElevatedButton(
                  onPressed: () {
                    // Adicionar ao carrinho
                  },
                  child: Icon(Icons.add_shopping_cart),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blueGrey,
                  ),
                ),
              ),
            );
          },
        );
      },
      loading: () => Center(child: CircularProgressIndicator()),
      error: (error, stack) => Center(child: Text('Error: $error')),
    );
  }
}