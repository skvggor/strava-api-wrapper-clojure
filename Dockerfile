FROM openjdk:19-jdk-buster

RUN apt-get update && apt-get install -y curl unzip

RUN curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein
RUN chmod 0755 /usr/local/bin/lein
RUN lein

WORKDIR /app

ADD . /app

COPY resources/.env resources/

RUN lein deps
RUN lein uberjar

CMD ["java", "-jar", "target/uberjar/strava-api.jar"]
