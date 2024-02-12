#!/bin/bash

# Start Minikube
# minikube start --driver=docker

# minikube status
kubectl config current-context

# Apply the configurations
echo "Starting all Kubernetes resources..."
kubectl apply -f mongodb-secrets.yaml
kubectl apply -f mongodb-deployment.yaml
kubectl apply -f mongodb-service.yaml
kubectl apply -f redis-deployment.yaml
kubectl apply -f redis-service.yaml
kubectl apply -f zookeeper-deployment.yaml
kubectl apply -f zookeeper-service.yaml
kubectl apply -f kafka-deployment.yaml
kubectl apply -f kafka-service.yaml

# First, you need to deploy local images for services
# eval $(minikube docker-env)
# docker build -t menu-service:1.0-1 .
# docker build -t orders-service:1.0-1 .
kubectl apply -f menu-api-deployment.yaml
kubectl apply -f menu-api-service.yaml
kubectl apply -f orders-api-deployment.yaml
kubectl apply -f orders-api-service.yaml

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
