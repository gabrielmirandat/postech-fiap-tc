import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class MenuScreen extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final menuAsyncValue = ref.watch(menuProvider);

    return menuAsyncValue.when(
      data: (menu) => Text(menu),
      loading: () => CircularProgressIndicator(),
      error: (error, stack) => Text('Error: $error'),
    );
  }
}
