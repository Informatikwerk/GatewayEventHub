FROM ubuntu:18.04

WORKDIR /opt

RUN apt-get update && \
        apt-get upgrade -y && \
        apt-get install -y  software-properties-common && \
        add-apt-repository ppa:webupd8team/java -y && \
        apt-get update && \
        echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
        apt-get install -y oracle-java8-installer && \
        apt-get clean
RUN apt-get update
RUN apt-get install -y wget gnupg
RUN apt-get install -y build-essential
RUN apt-get install -y nodejs
RUN apt-get install -y npm
RUN npm install -g npm@5.7.1
RUN /usr/bin/npm install -g gulp
RUN node -v

