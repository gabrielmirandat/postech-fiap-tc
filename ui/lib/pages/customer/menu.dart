import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../providers/cart-provider.dart';
import '../../providers/menu-provider.dart';

class MenuPage extends ConsumerWidget {
  final String category;
  final VoidCallback onItemSelected;

  MenuPage({required this.category, required this.onItemSelected});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final productState = ref.watch(productProvider(category));
    final cart = ref.watch(cartProvider); // Observa o carrinho

    return productState.when(
      data: (productList) {
        final filteredItems = productList.where((item) => item.category == category).toList();

        return ListView.builder(
          itemCount: filteredItems.length,
          itemBuilder: (context, index) {
            final item = filteredItems[index];
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
                    Text('${item.price.toStringAsFixed(2)} USD', style: TextStyle(color: Colors.green)),
                  ],
                ),
                trailing: ElevatedButton(
                  onPressed: () {
                    // Adiciona o item ao carrinho
                    cart.addItem(item);
                    onItemSelected(); // Próxima etapa
                  },
                  child: Icon(Icons.add_shopping_cart),
                  style: ElevatedButton.styleFrom(backgroundColor: Colors.blueGrey),
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