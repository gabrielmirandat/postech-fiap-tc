class MenuItemModel {
  final String name;
  final double price;

  MenuItemModel({
    required this.name,
    required this.price
  });

  static fromJson(Map<String, dynamic> json) {
    return MenuItemModel(
      name: json['name'],
      price: json['price']
    );
  }
}