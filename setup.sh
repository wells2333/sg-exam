#!/bin/bash
set -e

BASENAME=$(basename "$0")
SG_EXAM_WEB="sg-exam-web"
SG_EXAM_ADMIN="sg-exam-admin"
SG_EXAM_MOBILE="sg-exam-mobile"
SG_EXAM_USER_SERVICE="sg-user-service"
SG_EXAM_IMAGE_NAME="sg-exam"
DOWNLOAD_SG_EXAM_IMAGE_URL="http://116.205.237.35:9000/public"

function update_version() {
  local version="$1"
  echo "Updating project version to $version ..."
  sed -i "" "s/version = '[0-9]\{1,2\}.[0-9]\{1,2\}.[0-9]\{1,2\}'/version = '$version'/" build.gradle
  sed -i "" "s/version=[0-9]\{1,2\}.[0-9]\{1,2\}.[0-9]\{1,2\}/version=$version/" gradle.properties
  sed -i "" "s/version-[0-9]\{1,2\}.[0-9]\{1,2\}.[0-9]\{1,2\}/version-$version/" README.md
  cd sg-api && update_build "$version"
  cd ../sg-common && update_build "$version"
  cd ../sg-exam-service && update_build "$version"
  cd ../sg-generator && update_build "$version"
  cd ../sg-job && update_build "$version"
  cd ../sg-user-service && update_build "$version"
  cd ../frontend
  cd sg-exam-web && update_package_json "$version"
  cd ../sg-exam-admin && update_package_json "$version"
  cd ../sg-exam-mobile && update_package_json "$version"
  cd ../..
  echo "Project version has been updated to $version successfully."
}

function update_build() {
  local version="$1"
  sed -i "" "s/version '[0-9]\{1,2\}.[0-9]\{1,2\}.[0-9]\{1,2\}'/version '$version'/" build.gradle
}

function update_package_json() {
  local version="$1"
  sed -i "" "s/\"version\": \"[0-9]\{1,2\}.[0-9]\{1,2\}.[0-9]\{1,2\}\"/\"version\": \"$version\"/" package.json
}

function build_web() {
  echo "Building $SG_EXAM_WEB ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-web
  yarn build
  cd ../..
  echo "$SG_EXAM_WEB has been built successfully."
}

function build_admin() {
  echo "Building $SG_EXAM_ADMIN ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-admin
  yarn build
  cd ../..
  echo "$SG_EXAM_ADMIN has been built successfully."
}

function build_mobile() {
  echo "Building $SG_EXAM_MOBILE ..."
  # shellcheck disable=SC2164
  cd frontend/sg-exam-mobile
  export NODE_OPTIONS=--openssl-legacy-provider && yarn build:h5
  cd ../..
  echo "$SG_EXAM_MOBILE has been built successfully."
}

function build_frontend() {
  build_web
  build_admin
  build_mobile
}

function build_service() {
  local version="$1"
  echo "Building $SG_EXAM_USER_SERVICE $version ..."
  chmod 764 gradlew
  ./gradlew build -x test
  echo "$SG_EXAM_USER_SERVICE has been built successfully."
  echo "Building docker image ..."
  docker build -t "$SG_EXAM_IMAGE_NAME":"$version" -t "$SG_EXAM_IMAGE_NAME":latest .
  echo "Docker image has been built successfully."
}

function push_service() {
  local version="$1"
  echo "Pushing docker image ..."
  docker push "$SG_EXAM_IMAGE_NAME":"$version"
  docker push "$SG_EXAM_IMAGE_NAME":latest
  echo "Docker image has been pushed successfully."
}

function start_service() {
  echo "Starting services ..."
  docker-compose up --remove-orphans --no-build -d
  echo "Services has been started successfully."
  docker ps
  sleep 2s
  tail -f logs/sg-user-service.log
}

function start_service_inner() {
  echo "Starting nginx service ..."
  mkdir -p /apps/data/web/working/logs/nginx
  cd /usr/sbin
  ./nginx
  echo "Nginx service started."
  cd /apps/data/web/working
  java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=$TZ -Dfile.encoding=UTF-8 -jar app.jar
}

function stop_service() {
  echo "Stopping services ..."
  docker-compose stop -t 60
  echo "Services has been stopped successfully."
}

function install_docker() {
    yum install -y yum-utils device-mapper-persistent-data lvm2
    yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    yum makecache fast
    yum -y install docker-ce
    systemctl enable docker
    systemctl status docker
    service docker start
}

function setup() {
  echo "Start to setup, current directory: $(pwd)"
  if [ -e "minio.tar" ]; then
    echo "minio.tar exists."
  else
    echo "Download minio.tar ..."
    wget "$DOWNLOAD_SG_EXAM_IMAGE_URL"/minio.tar
  fi

  if [ -e "redis:latest.tar" ]; then
    echo "redis:latest.tar exists."
  else
    echo "Download redis:latest.tar ..."
    wget "$DOWNLOAD_SG_EXAM_IMAGE_URL"/redis:latest.tar
  fi

  if [ -e "mysql:5.7.27.tar" ]; then
    echo "mysql:5.7.27.tar exists."
  else
    echo "Download mysql:5.7.27.tar ..."
    wget "$DOWNLOAD_SG_EXAM_IMAGE_URL"/mysql:5.7.27.tar
  fi

  if [ -e "sg-exam:latest.tar" ]; then
    echo "sg-exam:latest.tar exists."
  else
    echo "Download sg-exam:latest.tar ..."
    wget "$DOWNLOAD_SG_EXAM_IMAGE_URL"/sg-exam:latest.tar
  fi

  docker load -i minio.tar
  docker load -i redis:latest.tar
  docker load -i mysql:5.7.27.tar
  docker load -i sg-exam:latest.tar
  echo "Setup images finished."

  local branch=master
  local repo_raw=https://raw.gitmirror.com/wells2333/sg-exam/"$branch"

  wget "$repo_raw"/docker-compose.yml
  wget -P config-repo "$repo_raw"/config-repo/application.yml
  wget -P config-repo "$repo_raw"/config-repo/sg-user-service.yml
  wget -P config-repo "$repo_raw"/config-repo/sg-exam.env
  wget -P config-repo/mysql "$repo_raw"/config-repo/mysql/init.sql
  wget -P config-repo/mysql "$repo_raw"/config-repo/mysql/update.sql
  wget -P config-repo/mysql/conf "$repo_raw"/config-repo/mysql/conf/my.cnf
  wget -P config-repo/nginx "$repo_raw"/config-repo/nginx/nginx.conf
  wget -P config-repo/redis "$repo_raw"/config-repo/redis/redis.conf

  echo "Setup directories ..."
  mkdir -p logs/nginx
  mkdir -p data/redis-data
  mkdir -p data/mysql-data
  mkdir -p data/minio/data
  mkdir -p data/minio/config
  echo "Setup finished."
}

function print_usage() {
  echo "Usage: $BASENAME COMMAND
       $BASENAME -help
  A online examination application.
  Commands:
      -help                Get detailed help and usage
      -build_f             Build frontend services
      -build               Build backend services and docker image
      -push                Push docker image to registry
      -start               Start services
      -stop                Stop services
      -restart             Pull docker image and restart services
      -version             Update project version to a specify version
      -setup               Setup docker deploy directory"
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
  -build_f | --build_f | build_f)
    build_frontend
    ;;
  -build | --build | build)
    build_service "$version"
    ;;
  -push | --push | push)
    push_service "$version"
    ;;
  -start | --start | start)
    start_service
    ;;
  -start_inner | --start_inner | start_inner)
    start_service_inner
    ;;
  -stop | --stop | stop)
    stop_service
    ;;
  -restart | --restart | restart)
    stop_service
    start_service
    ;;
  -version | --version | version)
    update_version "$version"
    ;;
  -setup | --setup | setup)
    setup
    ;;
  *)
    echo "$BASENAME: '$cmd' is not a $BASENAME command."
    echo "Run '$BASENAME -help' for more information."
    exit 1
    ;;
  esac
}
main "$@"
