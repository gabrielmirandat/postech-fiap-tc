import 'package:flutter/material.dart';

class PaymentStep extends StatelessWidget {
  final VoidCallback previousPage;

  PaymentStep({required this.previousPage});

  @override
  Widget build(BuildContext context) {
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
              // Exemplo de itens do pedido
              Text('1x X-Burguer - 10.90 USD'),
              Text('1x Coca-Cola - 3.50 USD'),
              Text('1x Pudim - 4.00 USD'),
              SizedBox(height: 20),
              Text('Total: 18.40 USD', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
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