import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CartPage extends StatelessWidget {
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
              // `cart` seria uma instância de algum modelo de carrinho de compras
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(cart.items[index].product.name),
                  subtitle: Text('Quantidade: ${cart.items[index].quantity}'),
                  trailing: Text('R\$ ${cart.items[index].totalPrice}'),
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
