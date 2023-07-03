#!/bin/bash

lein do deps, test, uberjar
java -jar target/uberjar/strava-api.jar
