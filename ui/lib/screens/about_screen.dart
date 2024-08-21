import 'package:flutter/material.dart';

class AboutScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('About Us'),
        backgroundColor: Colors.red,
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Text(
          'We are a restaurant committed to delivering the best food experience.',
          style: TextStyle(fontSize: 18.0),
        ),
      ),
    );
  }
}
