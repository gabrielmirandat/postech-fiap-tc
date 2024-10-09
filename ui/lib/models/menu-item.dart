class MenuItem {
  final String name;
  final double price;

  MenuItem({
    required this.name,
    required this.price
  });

  static fromJson(Map<String, dynamic> json) {
    return MenuItem(
      name: json['name'],
      price: json['price']
    );
  }
}