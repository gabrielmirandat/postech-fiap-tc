import 'package:flutter/material.dart';

class MenuScreen extends StatelessWidget {
  final List<Map<String, String>> menuItems = [
    {'name': 'Burger', 'price': '\$5.99'},
    {'name': 'Pizza', 'price': '\$8.99'},
    {'name': 'Pasta', 'price': '\$7.99'},
    {'name': 'Salad', 'price': '\$4.99'},
  ];

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: menuItems.length,
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(menuItems[index]['name']!),
          trailing: Text(menuItems[index]['price']!),
        );
      },
    );
  }
}
