#!/bin/bash

docker compose -p boki-portfolio up -d

# same as intelliJ environment variable
# java -jar -DJWT_SECRET=boki ./build/libs/*.jar --spring.profiles.active=dev
