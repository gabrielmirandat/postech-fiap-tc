import 'ingredient.dart';

class SupplierModel {
  late String id;
  late String name;
  late String contactInfo;
  late List<IngredientModel> productsSupplied;

  SupplierModel({
    required this.id,
    required this.name,
    required this.contactInfo,
    required this.productsSupplied,
  });

  static fromJson(Map<String, dynamic> json) {
    return SupplierModel(
      id: json['id'],
      name: json['name'],
      contactInfo: json['contactInfo'],
      productsSupplied: json['productsSupplied'].map<IngredientModel>((product) => IngredientModel.fromJson(product)).toList(),
    );
  }
}
