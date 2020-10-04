#!/bin/bash
commit=`git log -n 1 --oneline | sed 's/\(.......\).*/\1/'`
docker build -t andykuszyk/budjot:$commit .
docker push andykuszyk/budjot:$commit
git clone https://andykuszyk:$GITHUB_TOKEN@github.com/andykuszyk/do-docker
cd do-docker
make release-service SERVICE=budjot TAG=$commit
