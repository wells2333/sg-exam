#!/usr/bin/env bash

kubectl delete namespace logging

kubectl create namespace logging

kubectl apply -f kubernetes/efk/elasticsearch.yml -n logging
kubectl wait --timeout=120s --for=condition=Ready pod -n logging --all

kubectl apply -f kubernetes/efk/kibana.yml -n logging
kubectl wait --timeout=120s --for=condition=Ready pod -n logging --all

docker build -f kubernetes/efk/Dockerfile -t sg-exam/fluentd:v1 kubernetes/efk/

kubectl apply -f kubernetes/efk/fluentd-sg-exam-configmap.yml
kubectl apply -f kubernetes/efk/fluentd-ds.yml
kubectl wait --timeout=120s --for=condition=Ready pod -l app=fluentd -n kube-system

kubectl logs -n kube-system $(kubectl get pod -l app=fluentd -n kube-system -o jsonpath={.items..metadata.name}) | grep "fluentd worker is now running worker"

kubectl -n logging port-forward $(kubectl -n logging get pod -l run=kibana -o jsonpath='{.items[0].metadata.name}') 5601:5601

