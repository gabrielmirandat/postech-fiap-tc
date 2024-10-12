import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../providers/cart-provider.dart';

class ReviewOrderStep extends ConsumerWidget {
  final VoidCallback previousPage;
  final VoidCallback nextPage;

  ReviewOrderStep({required this.previousPage, required this.nextPage});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final cart = ref.watch(cartProvider);

    return Column(
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
            onPressed: nextPage,
            child: Text('Finalizar Pedido'),
          ),
        ),
      ],
    );
  }
}