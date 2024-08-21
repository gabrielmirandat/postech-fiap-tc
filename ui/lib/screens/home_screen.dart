import 'package:flutter/material.dart';

import '../widgets/footer.dart';
import '../widgets/header.dart';
import '../widgets/side_menu.dart';
import 'menu_screen.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(60.0),
        child: Header(),
      ),
      drawer: SideMenu(),
      body: MenuScreen(),
      bottomNavigationBar: Footer(),
    );
  }
}
