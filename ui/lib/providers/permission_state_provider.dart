import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'permission_notifier.dart';

// Provider do PermissionProvider
final permissionProvider = ChangeNotifierProvider((ref) => PermissionProvider());