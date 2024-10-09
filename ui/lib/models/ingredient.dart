import 'supplier.dart';

class Ingredient {
  late String id;
  late String name;
  late String category;
  late double price;
  late double weight;
  late bool isExtra;

  Ingredient({
    required this.id,
    required this.name,
    required this.category,
    required this.price,
    required this.weight,
    required this.isExtra,
  });

  static fromJson(Map<String, dynamic> json) {
    return Ingredient(
      id: json['id'],
      name: json['name'],
      category: json['category'],
      price: json['price'],
      weight: json['weight'],
      isExtra: json['isExtra'],
    );
  }
}
