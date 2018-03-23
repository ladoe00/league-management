#!/bin/bash

sudo docker rm $(sudo docker ps -a | grep Exited | awk '{print $1}')
MYSQL_ID=$(sudo docker ps | grep mysql | awk '{print $1}')
if [ -z "${MYSQL_ID}" ]; then
	sudo docker run --name mysql -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:latest 
fi

MYSQL_ID=$(sudo docker ps | grep mysql | awk '{print $1}')
echo "Waiting for mysql to startup..."
sleep 2
sudo docker exec -it ${MYSQL_ID} mysql -u root --password=secret
