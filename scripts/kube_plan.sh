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

# Plan Deployments
for deployment in "$DEPLOYMENTS_DIR"/*.yaml; do
    echo "Planning deployment from: $deployment"
    kubectl apply --dry-run=client -f "$deployment" --namespace="$NAMESPACE" || exit 1
done

# Plan Services
for service in "$SERVICES_DIR"/*.yaml; do
    echo "Planning service from: $service"
    kubectl apply --dry-run=client -f "$service" --namespace="$NAMESPACE" || exit 1
done