import 'package:flutter/material.dart';
import 'customer/order-step-process.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Restaurante - Faça seu pedido'),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
              child: Text('Menu do Restaurante'),
            ),
            ListTile(
              leading: Icon(Icons.menu),
              title: Text('Cardápio'),
              onTap: () {
                Navigator.of(context).pop();
              },
            ),
            ListTile(
              leading: Icon(Icons.info),
              title: Text('Sobre'),
              onTap: () {
                // Aqui você pode implementar uma rota para tela de informações
              },
            ),
          ],
        ),
      ),
      body: OrderStepProcess()
    );
  }
}