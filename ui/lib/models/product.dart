class ProductModel {
  late String id;
  late String name;
  late String description;
  late String category;
  late double price;
  late String image;

  ProductModel({
    required this.id,
    required this.name,
    required this.description,
    required this.category,
    required this.price,
    required this.image
  });

  // Método fromJson corrigido
  static ProductModel fromJson(Map<String, dynamic> json) {
    return ProductModel(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      category: json['category'],
      price: json['price'],
      image: json['image']
    );
  }
}