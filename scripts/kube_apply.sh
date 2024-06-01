#!/bin/bash

# Set the namespace
NAMESPACE="bookstore-ns"

# Create namespace if it doesn't exist
if ! kubectl get namespace "$NAMESPACE" &> /dev/null; then
    echo "Namespace $NAMESPACE does not exist. Creating..."
    kubectl create namespace "$NAMESPACE"
fi

# Define the deployments and services directories
DEPLOYMENTS_DIR="deployments"
SERVICES_DIR="services"

# Deploy Deployments
for deployment in "$DEPLOYMENTS_DIR"/*.yaml; do
    echo "Deploying: $deployment"
    kubectl apply -f "$deployment" --namespace="$NAMESPACE"
done

# Deploy Services
for service in "$SERVICES_DIR"/*.yaml; do
    echo "Deploying: $service"
    kubectl apply -f "$service" --namespace="$NAMESPACE"
done