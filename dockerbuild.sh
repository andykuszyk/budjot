#!/bin/bash
commit=`git log -n 1 --oneline | sed 's/\(.......\).*/\1/'`
echo "Building docker container..."
docker build -t andykuszyk/budjot:$commit .
echo "Pushing docker container..."
docker push andykuszyk/budjot:$commit
echo "Stopping any running containers..."
ssh -o "StrictHostKeyChecking no" -i id_rsa $douser@$doip "if [[ `docker container ls | sed '/CONT/d;s/\([a-z0-9]\+\).*/\1/' | wc -m` != 0 ]]; then docker stop `docker container ls | sed '/CONT/d;s/\([a-z0-9]\+\)\s\+.*/\1/'`; else echo 'no running containers found'; fi"
echo "Attempting to start new container..."
ssh -o "StrictHostKeyChecking no" -i id_rsa $douser@$doip "docker run -d -p 80:8080 andykuszyk/budjot:`echo $commit`"
