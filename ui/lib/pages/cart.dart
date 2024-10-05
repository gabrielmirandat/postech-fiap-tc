import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../models/cart.dart';

class CartPage extends StatelessWidget {
  final Cart cart; // Declare the cart variable

  CartPage({required this.cart}); // Constructor to receive the cart

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Carrinho'),
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: cart.items.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(cart.items[index].product.name),
                  subtitle: Text('Quantidade: ${cart.items[index].quantity}'),
                  trailing: Text('R\$ ${cart.items[index].totalPrice.toStringAsFixed(2)}'), // Format to 2 decimal places
                );
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: ElevatedButton(
              onPressed: () {
                // Finalizar pedido
              },
              child: Text('Finalizar Pedido'),
            ),
          ),
        ],
      ),
    );
  }
}
