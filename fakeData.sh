#!/bin/bash

# Create league
curl -X POST "http://localhost:8080/leagues?leagueName=NNHL" -H "accept: application/json"

#
# Create Users
#
# Forwards
curl -X POST "http://localhost:8080/users?email=lemieux66%40pens.com&firstName=Mario&lastName=Lemieux&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=crosby87%40pens.com&firstName=Sidney&lastName=Crosby&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=thegreatone%40oilers.com&firstName=Wayne&lastName=Gretzky&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=golden%40blues.com&firstName=Brett&lastName=Hull&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=koivu%40habs.com&firstName=Saku&lastName=Koivu&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=jotoews%40hawks.com&firstName=Jonathan&lastName=Toews&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=ovy8%40caps.com&firstName=Alex&lastName=Ovechkin&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=viking26%40habs.com&firstName=Mats&lastName=Naslund&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=stats%40nordiques.com&firstName=Peter&lastName=Statsny&password=secret&position=FORWARD" -H "accept: application/json"
# Defenseman
curl -X POST "http://localhost:8080/users?email=ricky%40sens.com&firstName=Erik&lastName=Karlsson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=larry%40habs.com&firstName=Larry&lastName=Robinson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=uncledrew%40kings.com&firstName=Drew&lastName=Doughty&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=pk%40preds.com&firstName=PK&lastName=Subban&password=secret&position=DEFENSEMAN" -H "accept: application/json"
# Goalies
curl -X POST "http://localhost:8080/users?email=onegoalie%40dfd.com&firstName=Carey&lastName=Price&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=themask%40dfd.com&firstName=Jacques&lastName=Plante&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=eddy%40dfd.com&firstName=Ed&lastName=Belfour&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/users?email=martybro%40dfd.com&firstName=Martin&lastName=Brodeur&password=secret&position=GOALIE" -H "accept: application/json"

#
# Subscription
#
curl -X POST "http://localhost:8080/leagues/1/users/1?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/2?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/3?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/4?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/5?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/6?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/7?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/8?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/9?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/10?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/11?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/12?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/13?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/14?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/15?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/16?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/leagues/1/users/17?subscription=SPARE" -H "accept: application/json"

#
# Start new season
#
curl -X POST "http://localhost:8080/games/newseason?leagueId=1&startDate=2018-01-01&numberOfGames=10" -H "accept: application/json"

echo
