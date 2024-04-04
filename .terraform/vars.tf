variable "region" {
  default = "us-east-1"
}

variable "lab-role-name" {
  default = "LabRole"
}

variable "principal-role-name" {
  default = "voclabs"
}

variable "cluster-name" {
  default = "gabscluster"
}

variable "node-name" {
  default = "gabsnode"
}

variable "policy-arn" {
  default = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
}

variable "access-config" {
  default = "API_AND_CONFIG_MAP"
}