output "region" {
  value = var.region
}

output "lab_role_arn" {
  value = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.lab-role-name}"
}

output "principal_role_arn" {
  value = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${var.principal-role-name}"
}

output "cluster-name" {
  value = aws_eks_cluster.cluster.name
}

output "cluster-endpoint" {
  value = aws_eks_cluster.cluster.endpoint
}

output "cluster-security-group-id" {
  value = aws_eks_cluster.cluster.vpc_config[0].cluster_security_group_id
}