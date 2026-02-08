import 'package:flutter/material.dart';
import 'customer/order-step-process.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Restaurant - Place your order'),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
              child: Text('Restaurant Menu'),
            ),
            ListTile(
              leading: Icon(Icons.menu),
              title: Text('Menu'),
              onTap: () {
                Navigator.of(context).pop();
              },
            ),
            ListTile(
              leading: Icon(Icons.info),
              title: Text('About'),
              onTap: () {
                // You can implement a route to the information screen here
              },
            ),
          ],
        ),
      ),
      body: OrderStepProcess()
    );
  }
}