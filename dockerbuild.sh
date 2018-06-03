#!/bin/bash
commit=`git log -n 1 --oneline | sed 's/\(.......\).*/\1/'`
docker build -t andykuszyk/budjot:$commit .
docker push andykuszyk/budjot:$commit
ssh -o "StrictHostKeyChecking no" -i id_rsa $douser@$doip "if [[ `docker container ls | sed '/CONT/d;s/\([a-z0-9]\+\).*/\1/' | wc -m` != 0 ]]; then docker stop `docker container ls | sed '/CONT/d;s/\([a-z0-9]\+\)\s\+.*/\1/'`;fi"
ssh -o "StrictHostKeyChecking no" -i id_rsa $douser@$doip "echo $commit > commit"
ssh -o "StrictHostKeyChecking no" -i id_rsa $douser@$doip "docker run -d -p 80:8080 andykuszyk/budjot:`cat ~/commit`"
