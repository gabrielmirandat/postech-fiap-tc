import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../providers/cart-provider.dart';

class CartPage extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final cart = ref.watch(cartProvider); // Obtém o cart diretamente do provider

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
                  trailing: Text('R\$ ${cart.items[index].totalPrice.toStringAsFixed(2)}'),
                );
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: ElevatedButton(
              onPressed: () {
                // Lógica para finalizar o pedido
                // Ex: enviar para API, limpar o carrinho, etc.
                cart.clearCart(); // Limpa o carrinho após finalizar
              },
              child: Text('Finalizar Pedido'),
            ),
          ),
        ],
      ),
    );
  }
}