#!/usr/bin/env bash

function waitForPods() {
    set +x
    local expectedPodCount=$1
    local labelSelection=$2
    local sleepSec=10

    n=0
    echo "Do we have $expectedPodCount pods with the label '$labelSelection' yet?"
    actualPodCount=$(kubectl get pod -l $labelSelection -o json | jq ".items | length")
    until [[ $actualPodCount == $expectedPodCount ]]
    do
        n=$((n + 1))
        if [[ $n == 40 ]]
        then
            echo " Give up"
            exit 1
        else
            echo -n "${actualPodCount}!=${expectedPodCount}, sleep $sleepSec..."
            sleep $sleepSec
            echo -n ", retry #$n, "
            actualPodCount=$(kubectl get pod -l $labelSelection -o json | jq ".items | length")
        fi
    done
    echo "OK! ($actualPodCount=$expectedPodCount)"
    set -x
}

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

kubectl create secret tls tls-certificate --key kubernetes/cert/tls.key --cert kubernetes/cert/tls.crt

kubectl create secret generic redis-credentials --from-literal=REDIS_HOST=redis --from-literal=REDIS_PORT=6379 --save-config

kubectl create secret generic mysql-credentials --from-literal=MYSQL_HOST=mysql --from-literal=SPRING_DATASOURCE_USERNAME=root --from-literal=SPRING_DATASOURCE_PASSWORD=sg-exam --save-config

kubectl label namespace sg-exam istio-injection=enabled

kubectl wait --timeout=600s --for=condition=ready pod --all

kubectl apply -k kubernetes/services/overlays/dev

kubectl wait --timeout=600s --for=condition=available deployment --all

waitForPods 5 'version=latest'

kubectl wait --timeout=120s --for=condition=Ready pod --all

