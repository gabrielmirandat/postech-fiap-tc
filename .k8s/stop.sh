#!/bin/bash

# Define the directory containing your Kubernetes YAML files
K8S_RESOURCE_DIR="."

# Delete the configurations for resources in reverse order of dependency
echo "Stopping all Kubernetes resources..."
kubectl delete -f $K8S_RESOURCE_DIR/orders-api-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/orders-api-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/menu-api-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/menu-api-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/kafka-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/kafka-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/zookeeper-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/zookeeper-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/redis-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/redis-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/mongodb-service.yaml
kubectl delete -f $K8S_RESOURCE_DIR/mongodb-deployment.yaml
kubectl delete -f $K8S_RESOURCE_DIR/mongodb-secrets.yaml
# Add any additional resources here in reverse order they were started

echo "All resources have been stopped and deleted."

# Stop Minikube
minikube stop
