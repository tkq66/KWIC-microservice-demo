minikube start;
sudo docker-compose up --build;
kubectl create -f deployment.yaml;
kubectl create -f service.yaml;
