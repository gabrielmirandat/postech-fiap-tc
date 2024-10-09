import 'ingredient.dart';

class Product {
  late String id;
  late String name;
  late String description;
  late String category;
  late double price;
  late String image;
  late List<Ingredient> ingredients;

  Product({
    required this.id,
    required this.name,
    required this.description,
    required this.category,
    required this.price,
    required this.image,
    required this.ingredients,
  });

  // Método fromJson corrigido
  static Product fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      category: json['category'],
      price: json['price'],
      image: json['image'],
      ingredients: (json['ingredients'] as List<dynamic>)
          .map<Ingredient>((ingredient) => Ingredient.fromJson(ingredient))
          .toList(),
    );
  }
}