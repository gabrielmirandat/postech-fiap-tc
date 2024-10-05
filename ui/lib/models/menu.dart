import 'menu-item.dart';

class Menu {
  final List<MenuItem> items;

  Menu({required this.items});

  factory Menu.fromJson(Map<String, dynamic> json) {
    var list = json['items'] as List;
    List<MenuItem> itemList = list.map((i) => MenuItem.fromJson(i)).toList();
    return Menu(items: itemList);
  }
}