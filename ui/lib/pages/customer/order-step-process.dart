import 'package:flutter/material.dart';
import 'cart.dart';

import 'step-payment.dart';
import 'step-review-order.dart';
import 'step-identification.dart';
import 'step-dessert.dart';
import 'step-drink.dart';
import 'step-accompaniment.dart';
import 'step-burger.dart';

class OrderStepProcess extends StatefulWidget {
  @override
  _OrderStepProcessState createState() => _OrderStepProcessState();
}

class _OrderStepProcessState extends State<OrderStepProcess> {
  PageController _pageController = PageController();
  int _currentPage = 0;

  void _nextPage() {
    _pageController.nextPage(duration: Duration(milliseconds: 300), curve: Curves.ease);
  }

  void _previousPage() {
    _pageController.previousPage(duration: Duration(milliseconds: 300), curve: Curves.ease);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Pedido - Etapa ${_currentPage + 1} de 7'),
      ),
      body: PageView(
        controller: _pageController,
        physics: NeverScrollableScrollPhysics(),
        onPageChanged: (page) {
          setState(() {
            _currentPage = page;
          });
        },
        children: [
          BurgerStep(nextPage: _nextPage),
          AccompanimentStep(nextPage: _nextPage, previousPage: _previousPage),
          DrinkStep(nextPage: _nextPage, previousPage: _previousPage),
          DessertStep(nextPage: _nextPage, previousPage: _previousPage),
          IdentificationStep(nextPage: _nextPage, previousPage: _previousPage),
          ReviewOrderStep(nextPage: _nextPage, previousPage: _previousPage),
          PaymentStep(previousPage: _previousPage),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => CartPage(), // Sem o parâmetro cart
            ),
          );
        },
        child: Icon(Icons.shopping_cart),
      ),
    );
  }
}