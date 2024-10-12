import 'package:flutter/material.dart';

class IdentificationStep extends StatelessWidget {
  final VoidCallback nextPage;
  final VoidCallback previousPage;

  IdentificationStep({required this.nextPage, required this.previousPage});

  @override
  Widget build(BuildContext context) {
    final _codeController = TextEditingController();

    return Column(
      children: [
        Text('Identifique-se', style: Theme.of(context).textTheme.headlineLarge),
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: TextField(
            controller: _codeController,
            decoration: InputDecoration(
              labelText: 'Digite o código enviado ao seu celular',
              border: OutlineInputBorder(),
            ),
            keyboardType: TextInputType.number,
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
              onPressed: () {
                // Valida o código de identificação aqui
                if (_codeController.text.isNotEmpty) {
                  nextPage();
                } else {
                  // Mostrar mensagem de erro
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Por favor, insira o código')),
                  );
                }
              },
              child: Text('Próximo'),
            ),
          ],
        ),
      ],
    );
  }
}