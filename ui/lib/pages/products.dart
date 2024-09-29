import 'package:flutter/material.dart';

class ProductsPage extends StatelessWidget {
  final String categoryId;

  ProductsPage({Key key, this.categoryId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Produtos'),
      ),
      body: FutureBuilder<List<Product>>(
        future: fetchProductsByCategory(categoryId),
        // Função que faz a chamada à API para obter produtos
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return ListView.builder(
              itemCount: snapshot.data?.length,
              itemBuilder: (context, index) {
                return ListTile(
                  leading: Image.network(snapshot.data?[index].imageUrl),
                  title: Text(snapshot.data?[index].name),
                  subtitle: Text('R\$ ${snapshot.data?[index].price}'),
                  onTap: () {
                    Navigator.pushNamed(context, '/productDetails',
                        arguments: snapshot.data[index].id);
                  },
                );
              },
            );
          } else if (snapshot.hasError) {
            return Center(child: Text('Erro ao carregar produtos.'));
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }

  fetchProductsByCategory(String categoryId) {}
}

class Product {}
