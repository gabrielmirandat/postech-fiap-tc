terraform {
  backend "remote" {
    organization = "com-gabriel-techchallenge"
    workspaces {
      name = "postech-fiap-tc"
    }
  }
}