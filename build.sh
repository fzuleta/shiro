#!/bin/bash 
cd $(dirname $0)

#first build shared libs
cd server
gradle build jar

#build client
cd ..
cd client
npm install
au build --env prod

cd ..

#Remove any running docker containers
docker rm -f -v fz_apigateway
docker rm -f -v fz_webserver
docker rm -f -v fz_members

# remove any images to start fresh
docker rmi fz/apigateway:latest
docker rmi fz/webserver:latest
docker rmi fz/members:latest

docker-compose up --build -d
