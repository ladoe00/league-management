#!/bin/bash

read -s -p "Enter admin password:" PASSWORD
echo
# Create league
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues?leagueName=NNHL" -H "accept: application/json"

#
# Create Players
#
# Forwards
curl -X POST "http://localhost:8080/players?email=lemieux66%40pens.com&firstName=Mario&lastName=Lemieux&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=crosby87%40pens.com&firstName=Sidney&lastName=Crosby&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=thegreatone%40oilers.com&firstName=Wayne&lastName=Gretzky&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=golden%40blues.com&firstName=Brett&lastName=Hull&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=koivu%40habs.com&firstName=Saku&lastName=Koivu&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=jotoews%40hawks.com&firstName=Jonathan&lastName=Toews&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=ovy8%40caps.com&firstName=Alex&lastName=Ovechkin&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=viking26%40habs.com&firstName=Mats&lastName=Naslund&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=stats%40nordiques.com&firstName=Peter&lastName=Statsny&password=secret&position=FORWARD" -H "accept: application/json"
# Defenseman
curl -X POST "http://localhost:8080/players?email=ricky%40sens.com&firstName=Erik&lastName=Karlsson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=larry%40habs.com&firstName=Larry&lastName=Robinson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=uncledrew%40kings.com&firstName=Drew&lastName=Doughty&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=pk%40preds.com&firstName=PK&lastName=Subban&password=secret&position=DEFENSEMAN" -H "accept: application/json"
# Goalies
curl -X POST "http://localhost:8080/players?email=onegoalie%40dfd.com&firstName=Carey&lastName=Price&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=themask%40dfd.com&firstName=Jacques&lastName=Plante&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=eddy%40dfd.com&firstName=Ed&lastName=Belfour&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/players?email=martybro%40dfd.com&firstName=Martin&lastName=Brodeur&password=secret&position=GOALIE" -H "accept: application/json"

#
# Subscription
#
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/1?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/2?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/3?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/4?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/5?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/6?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/7?subscription=SPARE" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/8?subscription=SPARE" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/9?subscription=SPARE" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/10?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/11?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/12?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/13?subscription=SPARE" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/14?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/15?subscription=REGULAR" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/16?subscription=SPARE" -H "accept: application/json"
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/leagues/1/players/17?subscription=SPARE" -H "accept: application/json"

#
# Start new season
#
curl -u admin:"${PASSWORD}" -X POST "http://localhost:8080/games/newseason?leagueId=1&startDate=2018-01-01&numberOfGames=10" -H "accept: application/json"

echo
