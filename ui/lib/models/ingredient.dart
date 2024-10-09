import 'supplier.dart';

class Ingredient {
  late String id;
  late String name;
  late double quantity;
  late String unit;
  late Supplier supplier;
  late double cost;
  late bool isAvailable;

  Ingredient({
    required this.id,
    required this.name,
    required this.quantity,
    required this.unit,
    required this.supplier,
    required this.cost,
    required this.isAvailable,
  });

  static fromJson(Map<String, dynamic> json) {
    return Ingredient(
      id: json['id'],
      name: json['name'],
      quantity: json['quantity'],
      unit: json['unit'],
      supplier: Supplier.fromJson(json['supplier']),
      cost: json['cost'],
      isAvailable: json['isAvailable'],
    );
  }
}
