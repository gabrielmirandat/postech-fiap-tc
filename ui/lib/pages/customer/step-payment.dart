import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../providers/cart-provider.dart';

class PaymentStep extends ConsumerWidget {
  final VoidCallback previousPage;

  PaymentStep({required this.previousPage});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final cart = ref.watch(cartProvider); // Obter o estado do carrinho

    return Column(
      children: [
        Text('Pagamento', style: Theme.of(context).textTheme.headlineLarge),
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text('Revisão do Pedido', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
              SizedBox(height: 10),

              // Listagem dos itens do carrinho
              ...cart.items.map((cartItem) => Text(
                '${cartItem.quantity}x ${cartItem.product.name} - R\$ ${(cartItem.totalPrice).toStringAsFixed(2)}',
              )),

              SizedBox(height: 20),
              Text(
                'Total: R\$ ${cart.totalPrice.toStringAsFixed(2)}',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ],
          ),
        ),
        SizedBox(height: 20),
        ElevatedButton(
          onPressed: () {
            // Implementar a lógica de pagamento aqui
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text('Pagamento realizado com sucesso!')),
            );
          },
          child: Text('Pagar'),
        ),
        SizedBox(height: 20),
        ElevatedButton(
          onPressed: previousPage,
          child: Text('Voltar'),
        ),
      ],
    );
  }
}