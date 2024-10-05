class MenuItem {
  final String name;
  final double price;

  MenuItem({required this.name, required this.price});

  factory MenuItem.fromJson(Map<String, dynamic> json) {
    return MenuItem(
      name: json['name'],
      price: (json['price'] as num).toDouble(),
    );
  }
}