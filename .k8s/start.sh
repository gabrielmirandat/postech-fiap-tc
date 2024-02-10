#!/bin/bash

# Define the directory containing your Kubernetes YAML files
K8S_RESOURCE_DIR="."

# Start Minikube
minikube start --driver=docker
minikube addons enable metrics-server

# minikube status
kubectl config current-context

# Apply the configurations
echo "Starting all Kubernetes resources..."
kubectl apply -f $K8S_RESOURCE_DIR/mongodb-secrets.yaml
kubectl apply -f $K8S_RESOURCE_DIR/mongodb-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/mongodb-service.yaml
kubectl apply -f $K8S_RESOURCE_DIR/redis-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/redis-service.yaml
kubectl apply -f $K8S_RESOURCE_DIR/zookeeper-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/zookeeper-service.yaml
kubectl apply -f $K8S_RESOURCE_DIR/kafka-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/kafka-service.yaml

# First, you need to deploy local images for services
# eval $(minikube docker-env)
# docker build -t menu-service:1.0 .
# docker build -t orders-service:1.0 .
kubectl apply -f $K8S_RESOURCE_DIR/menu-api-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/menu-api-service.yaml
kubectl apply -f $K8S_RESOURCE_DIR/orders-api-deployment.yaml
kubectl apply -f $K8S_RESOURCE_DIR/orders-api-service.yaml

# Start local servers
# Second, you need to enable node port for services
# echo "Starting local services resources..."
# MENU_SERVICE_URL=$(minikube service menu-service --url)
# echo $MENU_SERVICE_URL
# ORDERS_SERVICE_URL=$(minikube service orders-service --url)
# echo ORDERS_SERVICE_URL

echo "All resources have been started."
# List all resources
kubectl get all

# Open the Kubernetes dashboard
minikube dashboard
