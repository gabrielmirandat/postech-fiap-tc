import 'menu-item.dart';

class MenuModel {
  final List<MenuItemModel> items;

  MenuModel({
    required this.items
  });

  static fromJson(Map<String, dynamic> json) {
    return MenuModel(
      items: json['items'].map<MenuItemModel>((item) => MenuItemModel.fromJson(item)).toList()
    );
  }
}