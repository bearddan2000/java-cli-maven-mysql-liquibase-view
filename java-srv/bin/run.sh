#!/usr/bin/env bash

# A simple bash script to shorted docker CMD
# also allows for variables

mvn clean
mvn install
mvn compile
mvn exec:java
