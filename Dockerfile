FROM openjdk:19-jdk-buster

RUN apt-get update && apt-get install -y curl unzip && rm -rf /var/lib/apt/lists/*
RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein \
    && chmod 0755 /usr/local/bin/lein \
    && lein

WORKDIR /usr/src/app
COPY . /usr/src/app
COPY ./start.sh /usr/src/app/
CMD ["./start.sh"]
