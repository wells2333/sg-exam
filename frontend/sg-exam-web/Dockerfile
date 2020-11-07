FROM node:10-alpine as build-stage
WORKDIR /app
COPY package.json ./
ENV SASS_BINARY_SITE https://npm.taobao.org/mirrors/node-sass/
RUN npm install -g cnpm --registry=https://registry.npm.taobao.org
RUN npm install node-sass
COPY . .
RUN npm run build --silent

FROM nginx:1.17-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
