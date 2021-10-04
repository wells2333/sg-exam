#!/usr/bin/env bash

kubectl create deployment mail-server --image djfarrelly/maildev:1.1.0
kubectl expose deployment mail-server --port=80,25 --type=ClusterIP
kubectl wait --timeout=60s --for=condition=ready pod -l app=mail-server

kubectl -n sg-exam port-forward $(kubectl -n sg-exam get pod -l app=mail-server -o jsonpath='{.items[0].metadata.name}') 8080:80
