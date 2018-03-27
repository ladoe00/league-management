# league-management

How to start the database from Docker
---

1. Download and install Docker-CE (latest version)
1. If you are on Linux, simply run the script mysql.sh.  It will pull and start the mysql container and get a mysql prompt from that container. 
How to start the web-management application
1. If you are on Windows, I do not have a script yet (TODO).  You will have to perform the following:
``docker run --name mysql -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -d mysql:latest``
1. Run Main class `org.nnhl.ManagementApplication` from Eclipse with the following arguments: `server <YOUR PATH>/league-management/config.yml`
1. To check that your application is running enter url `http://localhost:8080/swagger#/`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
