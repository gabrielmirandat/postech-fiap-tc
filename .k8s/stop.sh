#!/bin/bash

# Delete the configurations for resources in reverse order of dependency
echo "Stopping all Kubernetes resources..."
kubectl delete -f orders-api-service.yaml
kubectl delete -f orders-api-deployment.yaml
kubectl delete -f menu-api-service.yaml
kubectl delete -f menu-api-deployment.yaml
kubectl delete -f kafka-service.yaml
kubectl delete -f kafka-deployment.yaml
kubectl delete -f zookeeper-service.yaml
kubectl delete -f zookeeper-deployment.yaml
kubectl delete -f redis-service.yaml
kubectl delete -f redis-deployment.yaml
kubectl delete -f mongodb-service.yaml
kubectl delete -f mongodb-deployment.yaml
kubectl delete -f mongodb-secrets.yaml
# Add any additional resources here in reverse order they were started

echo "All resources have been stopped and deleted."

# Stop Minikube
# minikube stop
