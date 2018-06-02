#!/bin/bash
commit=`git log -n 1 --oneline | sed 's/\(.......\).*/\1/'`
docker build -t andykuszyk/budjot:$commit .
docker push andykuszyk/budjot:$commit
ssh $douser@$doip "docker stop `docker container ls | sed 's/\([a-z0-9]\+\)\s\+.*/\1/'`"
ssh $douser@$doip "echo $commit >> commit"
ssh $douser@$doip "docker run -d -p 80:8080 andykuszyk/budjot:`cat commit`"
