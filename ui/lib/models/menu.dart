import 'menu-item.dart';

class Menu {
  final List<MenuItem> items;

  Menu({
    required this.items
  });

  static fromJson(Map<String, dynamic> json) {
    return Menu(
      items: json['items'].map<MenuItem>((item) => MenuItem.fromJson(item)).toList()
    );
  }
}