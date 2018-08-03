#!/bin/bash
commit=`git log -n 1 --oneline | sed 's/\(.......\).*/\1/'`
docker build -t andykuszyk/budjot:$commit .
docker push andykuszyk/budjot:$commit
sed -i "s/\[mongouser\]/$mongoUser/g" docker-compose.yml
sed -i "s/\[mongopassword\]/$mongoPassword/g" docker-compose.yml
sed -i "s/\[version\]/$commit/g" docker-compose.yml
scp -i id_rsa -o "StrictHostKeyChecking no" ./docker-compose.yml $douser@$doip:~/docker-compose.yml
ssh -i id_rsa -o "StrictHostKeyChecking no" $douser@$doip "docker stack deploy -c ~/docker-compose.yml szyk" 
