import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ReviewOrderStep extends StatelessWidget {
  final VoidCallback nextPage;
  final VoidCallback previousPage;

  ReviewOrderStep({required this.nextPage, required this.previousPage});

  @override
  Widget build(BuildContext context) {
    // Exibir os itens adicionados ao carrinho para revisão
    return Column(
      children: [
        Expanded(
          child: ListView(
            children: [
              // Adicione aqui os detalhes do pedido/carrinho
              ListTile(
                title: Text('X-Big'),
                subtitle: Text('10.90 USD'),
              ),
              // Outros itens...
            ],
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            ElevatedButton(onPressed: previousPage, child: Text('Voltar')),
            ElevatedButton(onPressed: nextPage, child: Text('Finalizar Pedido')),
          ],
        ),
      ],
    );
  }
}