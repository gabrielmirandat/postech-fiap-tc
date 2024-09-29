import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ProductDetailsPage extends StatelessWidget {
  final String productId;

  ProductDetailsPage({Key key, this.productId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Detalhes do Produto'),
      ),
      body: FutureBuilder<Product>(
        future: fetchProductDetails(productId),
        // Função que faz a chamada à API para obter detalhes do produto
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return Column(
              children: [
                Image.network(snapshot.data.imageUrl),
                Text(snapshot.data.name, style: TextStyle(fontSize: 24)),
                Text('R\$ ${snapshot.data.price}',
                    style: TextStyle(fontSize: 20)),
                Text(snapshot.data.description),
                ElevatedButton(
                  onPressed: () {
                    // Adicionar ao carrinho
                  },
                  child: Text('Adicionar ao Carrinho'),
                ),
              ],
            );
          } else if (snapshot.hasError) {
            return Center(child: Text('Erro ao carregar detalhes do produto.'));
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }
}
