#!/bin/sh

# Build the project
mvn clean install 
# Build Docker File
docker build -t siddsatish95/chairwise-input-service:test .;
# Push to DockerHub Registry
docker push siddsatish95/chairwise-input-service:test;
# Create Persistence Volume for DB
kubectl apply -f mongo-pvc.yaml
# Create Deployment mongodb
kubectl apply -f mongo-deployment.yaml
# Exposing Deployment through service to access from outside world
# kubectl port-forward deployment/mongodb 60001:27017 &
# Create Deployment chairwise-input-service
kubectl apply -f deployment.yml;
# Create service chairwise-input-service
kubectl apply -f service.yml;
# Obtain Cluster IP and port on localhost to which service is exposed to
# kubectl port-forward deployment/chairwise-input-service 60002:8080 &

# Obtain IP Address of Minikube
# minikube ip

# Now API can be accessed from URL
# http://<minikube_ip>:<service_port>/api/v1

##########################
# Delete all Components
##########################

# kubectl delete deployment chairwise-cshift-service
# kubectl delete service chairwise-cshift-service
# kubectl delete service chairwise-input-service
# kubectl delete deployment chairwise-input-service
# kubectl delete deployment chairwise-main-service
# kubectl delete service chairwise-main-service
# kubectl delete service chairwise-output-service
# kubectl delete deployment chairwise-output-service
# kubectl delete deployment chairwise-sorting-service
# kubectl delete service chairwise-sorting-service
# kubectl delete service mongodb
# kubectl delete deployment mongodb
# kubectl delete pvc mongodata

##########################
# Create Components
##########################

# kubectl apply -f mongo-pvc.yaml
# kubectl apply -f mongo-deployment.yaml
# kubectl expose deployment mongodb --type=LoadBalancer
# kubectl get svc
# minikube ip
# Use IP and Port to Access MongoDB and replace in all JAR's

# Input Service
# docker build -t siddsatish95/chairwise-input-service:final .
# docker push siddsatish95/chairwise-input-service:final
# kubectl apply -f deployment.yaml
# kubectl apply -f service.yaml
# kubectl get svc

# Circular Shift Service
# docker build -t siddsatish95/chairwise-cshift-service:final .
# docker push siddsatish95/chairwise-cshift-service:final
# kubectl apply -f deployment.yml
# kubectl apply -f service.yml
# kubectl get svc

# Sorting Service
# docker build -t siddsatish95/chairwise-sorting-service:final .
# docker push siddsatish95/chairwise-sorting-service:final
# kubectl apply -f deployment.yml
# kubectl apply -f service.yml
# kubectl get svc

# Output Service
# docker build -t siddsatish95/chairwise-output-service:final .
# docker push siddsatish95/chairwise-output-service:final
# kubectl apply -f deployment.yml
# kubectl apply -f service.yml
# kubectl get svc

# Main Service
# Replace Ports and IP of each of the service in KWICService file
# docker build -t siddsatish95/chairwise-main-service:final .
# docker push siddsatish95/chairwise-main-service:final
# kubectl apply -f deployment.yml
# kubectl apply -f service.yml
# kubectl get svc

# To expose container to localhost
# docker run -it --expose 8503 -p 8008:8503 siddsatish95/chairwise-main-service:final