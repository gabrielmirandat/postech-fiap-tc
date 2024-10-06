class LoginState {
  final bool loading;
  final String? error;
  final bool isAuthenticated;
  final String? username;

  LoginState({
    this.loading = false,
    this.error,
    this.isAuthenticated = false,
    this.username,
  });
}