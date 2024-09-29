import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

class CategoriesPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Categorias'),
      ),
      body: FutureBuilder<List<Category>>(
        future: fetchCategories(),
        // Função que faz a chamada à API para obter categorias
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return ListView.builder(
              itemCount: snapshot.data?.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(snapshot.data?[index].name),
                  onTap: () {
                    Navigator.pushNamed(context, '/products',
                        arguments: snapshot.data?[index].id);
                  },
                );
              },
            );
          } else if (snapshot.hasError) {
            return Center(child: Text('Erro ao carregar categorias.'));
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }

  fetchCategories() {}
}
