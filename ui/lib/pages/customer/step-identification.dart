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
        Text('Identify yourself', style: Theme.of(context).textTheme.headlineLarge),
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: TextField(
            controller: _codeController,
            decoration: InputDecoration(
              labelText: 'Enter the code sent to your phone',
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
              child: Text('Back'),
            ),
            ElevatedButton(
              onPressed: () {
                // Validate the identification code here
                if (_codeController.text.isNotEmpty) {
                  nextPage();
                } else {
                  // Show error message
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Please enter the code')),
                  );
                }
              },
              child: Text('Next'),
            ),
          ],
        ),
      ],
    );
  }
}