import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'menu.dart';

class AccompanimentStep extends ConsumerWidget {
  final VoidCallback nextPage;
  final VoidCallback previousPage;

  AccompanimentStep({required this.nextPage, required this.previousPage});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: [
        Expanded(
          child: MenuPage(
            category: 'accompaniment',
            onItemSelected: () {
              // Adicionar ao carrinho
            },
          ),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            ElevatedButton(
              onPressed: previousPage,
              child: Text('Voltar'),
            ),
            ElevatedButton(
              onPressed: nextPage,
              child: Text('Próxima Etapa: Bebidas'),
            ),
          ],
        ),
      ],
    );
  }
}