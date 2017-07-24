FROM openjdk:alpine

MAINTAINER Felipe Zuleta 
ARG mainDir=apigateway
ARG mainJar=apigateway-1.0.jar

COPY ./server/$mainDir/build/libs/$mainJar /opt/$mainJar
COPY ./.docker/$mainDir/firstrun.sh /
COPY ./resources/self.keystore /opt/shiro/self.keystore

ENV MAINJAR=$mainJar

WORKDIR /opt/

RUN apk update && \
    apk upgrade && \
    apk add nano && \
    apk add --update bash && \
    rm -rf /var/cache/apk/* && \
    chmod +x /firstrun.sh && \
    chmod +x /opt/$mainJar

ENTRYPOINT ["/firstrun.sh"]