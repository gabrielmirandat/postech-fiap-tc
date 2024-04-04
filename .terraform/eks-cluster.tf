resource "aws_eks_cluster" "cluster" {
  name     = var.cluster-name
  role_arn = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.lab-role-name}"

  vpc_config {
    subnet_ids         = data.aws_subnets.subnets.ids
    security_group_ids = [data.aws_security_group.sg.id]
  }

  access_config {
    authentication_mode = var.access-config
  }
}