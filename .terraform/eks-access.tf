resource "aws_eks_access_entry" "access" {
  cluster_name      = aws_eks_cluster.cluster.name
  principal_arn     = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.principal-role-name}"
  kubernetes_groups = ["group-1", "group-2"]
  type              = "STANDARD"
}