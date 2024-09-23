#!/bin/bash -xe

mvn clean package -DskipTests
docker build -f Dockerfile.jvm -t guiilhermego/museubarroco2 .