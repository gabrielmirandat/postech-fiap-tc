resource "aws_eks_node_group" "node" {
  cluster_name    = aws_eks_cluster.cluster.name
  node_group_name = var.node-name
  node_role_arn   = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.lab-role-name}"
  subnet_ids      = data.aws_subnets.subnets.ids
  disk_size       = 50
  instance_types  = ["t3.medium"]

  scaling_config {
    desired_size = 1
    max_size     = 5
    min_size     = 1
  }

  update_config {
    max_unavailable = 1
  }
}