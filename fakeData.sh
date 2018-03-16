#!/bin/bash


# Forwards
curl -X POST "http://localhost:8080/user?firstName=Mario&lastName=Lemieux&email=lemieux66%40pens.com&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user?firstName=Sidney&lastName=Crosby&email=crosby87%40pens.com&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user?firstName=Wayne&lastName=Gretzky&email=thegreatone%40oilers.com&password=secret&position=FORWARD" -H "accept: application/json"

# Goalies
curl -X POST "http://localhost:8080/user?firstName=Carey&lastName=Price&email=onegoalie%40dfd.com&password=secret&position=GOALIE" -H "accept: application/json"
