resource "aws_eks_access_policy_association" "policy" {
  cluster_name  = aws_eks_cluster.cluster.name
  policy_arn    = var.policy-arn
  principal_arn = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.principal-role-name}"

  access_scope {
    type = "cluster"
  }
}