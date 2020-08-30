FROM node:latest as build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install -g cnpm --registry=https://registry.npm.taobao.org
RUN npm install node-sass --sass\_binary\_site=https://npm.taobao.org/mirrors/node-sass/

COPY ./ .

RUN npm run build

FROM nginx as production-stage

RUN mkdir /app

COPY --from=build-stage /app/dist /app
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
