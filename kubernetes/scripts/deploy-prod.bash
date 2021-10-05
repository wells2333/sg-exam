#!/usr/bin/env bash

kubectl delete namespace sg-exam

docker rmi $(docker images | grep "none" | awk '{print $3}')

kubectl create namespace sg-exam

kubectl config set-context $(kubectl config current-context) --namespace=sg-exam

if kubectl -n istio-system get secret istio-ingressgateway-certs > /dev/null ; then
    echo "Secret istio-ingressgateway-certs found, skip creating it..."
else
    echo "Secret istio-ingressgateway-certs not found, creating it..."
    kubectl create -n istio-system secret tls istio-ingressgateway-certs \
        --key kubernetes/cert/tls.key \
        --cert kubernetes/cert/tls.crt
fi

kubectl create configmap config-repo-auth-service      --from-file=config-repo/application.yml --from-file=config-repo/auth-service.yml --save-config
kubectl create configmap config-repo-exam-service      --from-file=config-repo/application.yml --from-file=config-repo/exam-service.yml --save-config
kubectl create configmap config-repo-msc-service      --from-file=config-repo/application.yml --from-file=config-repo/msc-service.yml --save-config
kubectl create configmap config-repo-user-service      --from-file=config-repo/application.yml --from-file=config-repo/user-service.yml --save-config

kubectl create secret generic config-server-secrets --from-literal=ENCRYPT_KEY=my-very-secure-encrypt-key --from-literal=SPRING_SECURITY_USER_NAME=dev-usr --from-literal=SPRING_SECURITY_USER_PASSWORD=dev-pwd --save-config

kubectl create secret generic redis-credentials --from-literal=SPRING_REDIS_HOST=redis --from-literal=SPRING_REDIS_PORT=6379 --save-config

kubectl create secret generic mysql-credentials --from-literal=SPRING_DATASOURCE_USERNAME=root --from-literal=SPRING_DATASOURCE_PASSWORD=abc123456 --save-config

kubectl label namespace sg-exam istio-injection=enabled

kubectl create secret tls tls-certificate --key kubernetes/cert/tls.key --cert kubernetes/cert/tls.crt

# Deploy v1 services
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/app-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/app-service:v1
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/admin-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/admin-service:v1
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/auth-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/auth-service:v1
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/user-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/user-service:v1
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/exam-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/exam-service:v1
docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/msc-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/msc-service:v1

kubectl apply -k kubernetes/services/base/services
kubectl apply -k kubernetes/services/overlays/prod/common
kubectl apply -k kubernetes/services/overlays/prod/v1
kubectl apply -k kubernetes/services/overlays/prod/istio
kubectl wait --timeout=180s --for=condition=ready pod --all

# Deploy v2 services
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/app-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/app-service:v2
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/admin-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/admin-service:v2
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/auth-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/auth-service:v2
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/user-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/user-service:v2
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/exam-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/exam-service:v2
#docker tag registry.cn-hangzhou.aliyuncs.com/sg-exam/msc-service   registry.cn-hangzhou.aliyuncs.com/sg-exam/msc-service:v2

#kubectl apply -k kubernetes/services/overlays/prod/v2
#kubectl wait --timeout=180s --for=condition=ready pod --all

