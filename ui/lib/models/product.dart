import 'ingredient.dart';

class Product {
  late String id;
  late String name;
  late String description;
  late double price;
  late String category;
  late List<Ingredient> ingredients;
  late bool isAvailable;
  late String imageUrl;

  Product({
    required this.id,
    required this.name,
    required this.description,
    required this.price,
    required this.category,
    required this.ingredients,
    required this.isAvailable,
    required this.imageUrl,
  });

  static fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      price: json['price'],
      category: json['category'],
      ingredients: json['ingredients'].map<Ingredient>((item) => Ingredient.fromJson(item)).toList(),
      isAvailable: json['isAvailable'],
      imageUrl: json['imageUrl'],
    );
  }
}
