#!/bin/bash
function build_web() {
  # build web
  echo "start to build web"
  # shellcheck disable=SC2164
  cd frontend/sg-exam-app
  yarn build
  cd ../..
  echo "build web finished"
}
function build_admin() {
  # build admin
  echo "start to build admin"
  # shellcheck disable=SC2164
  cd frontend/sg-exam-next-admin
  yarn build
  cd ../..
  echo "build admin finished"
}
function build_app() {
  # build app
  echo "start to build app"
  # shellcheck disable=SC2164
  cd frontend/sg-exam-next-app
  yarn build:weapp
  cd ../..
  echo "build app finished"
}
function build_frontend() {
  build_web
  build_admin
  build_app
}

function build_service() {
  # build service
  echo "start to build service"
  chmod 764 gradlew
  ./gradlew build
  echo "build service finished, start to build image"
  echo "start to build service image"
  docker-compose build --parallel
  echo "build service image finished"
}

function push_service() {
  echo "start to push service"
  docker-compose push
  echo "push service finished"
}
function start_service() {
  # pull image
  echo "start to pull image"
  docker-compose pull
  echo "start to start service with config"
  docker-compose config
  docker-compose up --remove-orphans --no-build -d
  echo "start service finished"
  # ps
  docker ps
  # tail logs
  logs
}

function stop_service() {
  echo "start to stop service"
  docker-compose stop
  echo "stop service finished"
}

function logs() {
  docker logs "$(docker ps |grep 'user-service'|awk '{print $1}')" -f --tail=100
}

function main() {
  clear
  if [ "$#" -eq 0 ]; then
    _usage
    exit 0
  fi
  local cmd="$1"
  shift
  case "${cmd}" in
  build_f)
    build_frontend
    ;;
  build_admin)
      build_admin
      ;;
  build)
    build_service
    ;;
  push)
    push_service
    ;;
  start)
    start_service
    ;;
  stop)
    stop_service
    ;;
  restart)
    stop_service
    start_service
    ;;
  logs)
    logs
    ;;
  esac
}
main "$@"
