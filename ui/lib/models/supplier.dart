import 'ingredient.dart';

class Supplier {
  late String id;
  late String name;
  late String contactInfo;
  late List<Ingredient> productsSupplied;

  Supplier({
    required this.id,
    required this.name,
    required this.contactInfo,
    required this.productsSupplied,
  });

  static fromJson(Map<String, dynamic> json) {
    return Supplier(
      id: json['id'],
      name: json['name'],
      contactInfo: json['contactInfo'],
      productsSupplied: json['productsSupplied'].map<Ingredient>((product) => Ingredient.fromJson(product)).toList(),
    );
  }
}
