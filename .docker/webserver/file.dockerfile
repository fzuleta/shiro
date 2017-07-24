FROM openjdk:alpine

MAINTAINER Felipe Zuleta 
ARG mainDir=webserver
ARG mainJar=webserver-1.0.jar

COPY ./server/$mainDir/build/libs/$mainJar /opt/$mainJar
COPY ./.docker/$mainDir/firstrun.sh /
COPY ./resources/self.keystore /opt/shiro/self.keystore

# Copy aurelia stuff ------------------------------------------------
COPY ./client/index.html /opt/aurelia/
COPY ./client/images /opt/aurelia/images
COPY ./client/locales /opt/aurelia/locales
COPY ./client/scripts /opt/aurelia/scripts
# -------------------------------------------------------------------

ENV MAINJAR=$mainJar

EXPOSE 8888 8443

WORKDIR /opt/

RUN apk update && \
    apk upgrade && \
    apk add nano && \
    apk add --update bash && \
    rm -rf /var/cache/apk/* && \
    chmod +x /firstrun.sh && \
    chmod +x /opt/$mainJar

ENTRYPOINT ["/firstrun.sh"]