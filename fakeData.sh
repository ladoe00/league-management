#!/bin/bash

# Create league
curl -X POST "http://localhost:8080/league/NNHL" -H "accept: application/json"

#
# Create Users
#
# Forwards
curl -X POST "http://localhost:8080/user/lemieux66%40pens.com?firstName=Mario&lastName=Lemieux&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/crosby87%40pens.com?firstName=Sidney&lastName=Crosby&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/thegreatone%40oilers.com?firstName=Wayne&lastName=Gretzky&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/golden%40blues.com?firstName=Brett&lastName=Hull&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/koivu%40habs.com?firstName=Saku&lastName=Koivu&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/jotoews%40hawks.com?firstName=Jonathan&lastName=Toews&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/ovy8%40caps.com?firstName=Alex&lastName=Ovechkin&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/viking26%40habs.com?firstName=Mats&lastName=Naslund&password=secret&position=FORWARD" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/stats%40nordiques.com?firstName=Peter&lastName=Statsny&password=secret&position=FORWARD" -H "accept: application/json"
# Defenseman
curl -X POST "http://localhost:8080/user/ricky%40sens.com?firstName=Erik&lastName=Karlsson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/larry%40habs.com?firstName=Larry&lastName=Robinson&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/uncledrew%40kings.com?firstName=Drew&lastName=Doughty&password=secret&position=DEFENSEMAN" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/pk%40preds.com?firstName=PK&lastName=Subban&password=secret&position=DEFENSEMAN" -H "accept: application/json"
# Goalies
curl -X POST "http://localhost:8080/user/onegoalie%40dfd.com?firstName=Carey&lastName=Price&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/themask%40dfd.com?firstName=Jacques&lastName=Plante&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/eddy%40dfd.com?firstName=Ed&lastName=Belfour&password=secret&position=GOALIE" -H "accept: application/json"
curl -X POST "http://localhost:8080/user/martybro%40dfd.com?firstName=Martin&lastName=Brodeur&password=secret&position=GOALIE" -H "accept: application/json"

#
# Subscription
#
curl -X POST "http://localhost:8080/league/NNHL/user/lemieux66%40pens.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/crosby87%40pens.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/thegreatone%40oilers.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/golden%40blues.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/koivu%40habs.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/jotoews%40hawks.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/ovy8%40caps.com?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/viking26%40habs.com?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/stats%40nordiques.com?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/ricky%40sens.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/larry%40habs.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/uncledrew%40kings.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/pk%40preds.com?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/onegoalie%40dfd.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/themask%40dfd.com?subscription=REGULAR" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/eddy%40dfd.com?subscription=SPARE" -H "accept: application/json"
curl -X POST "http://localhost:8080/league/NNHL/user/martybro%40dfd.com?subscription=SPARE" -H "accept: application/json"

#
# Start new season
#
curl -X POST "http://localhost:8080/league/NNHL/season?startDate=2018-01-01&numberOfGames=10" -H "accept: application/json"

echo
