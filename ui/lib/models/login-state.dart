class LoginState {
  final bool loading;
  final String? error;
  final bool isAuthenticated;
  final String? username;
  final String token;
  final num expiresIn;

  LoginState({
    this.loading = false,
    this.error,
    this.isAuthenticated = false,
    this.username,
    this.token = '',
    this.expiresIn = 0,
  });
}