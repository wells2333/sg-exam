#!/usr/bin/env bash

echo "start build..."

chmod 764 gradlew
./gradlew build

echo "build finished, start to build image"

docker-compose build --parallel

echo "build image finished, start to push image"

docker-compose push

echo "all build finished, exit"