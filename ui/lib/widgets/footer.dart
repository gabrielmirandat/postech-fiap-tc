import 'package:flutter/material.dart';

class Footer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.red,
      height: 50.0,
      child: Center(
        child: Text(
          '© 2024 Restaurant Name. All Rights Reserved.',
          style: TextStyle(color: Colors.white),
        ),
      ),
    );
  }
}
