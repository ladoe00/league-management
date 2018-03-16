#!/bin/bash

sudo docker run -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:latest 
