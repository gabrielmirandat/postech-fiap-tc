import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'menu.dart';

class BurgerStep extends ConsumerWidget {
  final VoidCallback nextPage;

  BurgerStep({required this.nextPage});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return Column(
      children: [
        Expanded(
          child: MenuPage(
            category: 'burger',
            onItemSelected: () {
              // O item é adicionado ao carrinho diretamente no Menu
            },
          ),
        ),
        ElevatedButton(
          onPressed: nextPage,
          child: Text('Próxima Etapa: Acompanhamentos'),
        ),
      ],
    );
  }
}