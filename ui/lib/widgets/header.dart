import 'package:flutter/material.dart';

class Header extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return AppBar(
      title: Text('Welcome to Our Restaurant'),
      centerTitle: true,
      backgroundColor: Colors.red,
    );
  }
}
