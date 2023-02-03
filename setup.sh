#!/bin/bash
set -e

BASENAME=$(basename "$0")
SG_EXAM_APP="sg-exam-app"
SG_EXAM_NEXT_ADMIN="sg-exam-next-admin"
SG_EXAM_NEXT_APP="sg-exam-next-app"
SG_EXAM_USER_SERVICE="sg-exam-user-service"

function update_version() {
  local version="$1"
  echo "Updating project version to $version ..."
  sed -i "" "s/version = '[0-9].[0-9].[0-9]'/version = '$version'/" build.gradle
  sed -i "" "s/version=[0-9].[0-9].[0-9]/version=$version/" gradle.properties
  sed -i "" "s/SG_EXAM_VERSION=[0-9].[0-9].[0-9]/SG_EXAM_VERSION=$version/" .env
  sed -i "" "s/version-[0-9].[0-9].[0-9]/version-$version/" README.md
  cd sg-api && update_build "$version"
  cd ../sg-common && update_build "$version"
  cd ../sg-exam-service && update_build "$version"
  cd ../sg-generator && update_build "$version"
  cd ../sg-job && update_build "$version"
  cd ../sg-user-service && update_build "$version"
  cd ../frontend
  cd sg-exam-app && update_package_json "$version"
  cd ../sg-exam-next-admin && update_package_json "$version"
  cd ../sg-exam-next-app && update_package_json "$version"
  cd ../..
  echo "Project version has been updated to $version successfully."
}

function update_build() {
  local version="$1"
  sed -i "" "s/version '[0-9].[0-9].[0-9]'/version '$version'/" build.gradle
}

function update_package_json() {
  local version="$1"
  sed -i "" "s/\"version\": \"[0-9].[0-9].[0-9]\"/\"version\": \"$version\"/" package.json
}

function build_web() {
  echo "Building $SG_EXAM_APP ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-app
  yarn build
  cd ../..
  echo "$SG_EXAM_APP has been built successfully."
}

function build_admin() {
  echo "Building $SG_EXAM_NEXT_ADMIN ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-next-admin
  yarn build
  cd ../..
  echo "$SG_EXAM_NEXT_ADMIN has been built successfully."
}

function build_app() {
  echo "Building $SG_EXAM_NEXT_APP ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-next-app
  export NODE_OPTIONS=--openssl-legacy-provider && yarn build:weapp
  cd ../..
  echo "$SG_EXAM_NEXT_APP has been built successfully."
}

function build_frontend() {
  build_web
  build_admin
  build_app
}

function build_service() {
  echo "Building $SG_EXAM_USER_SERVICE ..."
  chmod 764 gradlew
  ./gradlew clean
  ./gradlew build
  echo "$SG_EXAM_USER_SERVICE has been built successfully."
  echo "Building docker image ..."
  docker-compose build
  echo "Docker image has been built successfully."
}

function push_service() {
  echo "Pushing docker image ..."
  docker-compose push
  echo "Docker image has been pushed successfully."
}

function start_service() {
  echo "Pulling docker image ..."
  docker-compose pull
  echo "Starting services ..."
  docker-compose config
  docker-compose up --remove-orphans --no-build -d
  echo "Services has been started successfully."
  docker ps
  logs
}

function stop_service() {
  echo "Stopping services ..."
  docker-compose stop
  echo "Services has been stopped successfully."
}

function logs() {
  docker logs "$(docker ps |grep $SG_EXAM_USER_SERVICE|awk '{print $1}')" -f --tail=100
}

function print_usage() {
  echo "Usage: $BASENAME COMMAND
       $BASENAME --help
  A online examination application.
  Commands:
      help                Get detailed help and usage
      build_f             Build frontend services
      build               Build backend services and docker image
      push                Push docker image to registry
      start               Start services
      stop                Stop services
      restart             Pull docker image and restart services
      logs                Tails the services logs
      version             Update project version to a specify version"
  exit 1
}

function main() {
  if [ "$#" -eq 0 ]; then
    _usage
    exit 0
  fi
  local cmd="$1"
  local version="$2"
  shift
  case "${cmd}" in
  -h | --help | help)
    print_usage
    exit
    ;;
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
  version)
    update_version "$version"
    ;;
  *)
    echo "$BASENAME: '$cmd' is not a $BASENAME command."
    echo "Run '$BASENAME --help' for more information."
    exit 1
    ;;
  esac
}
main "$@"
