@echo off

SET PASSWORD=%1

for /f %%i in ('curl -s -u admin:%PASSWORD% -X GET "http://localhost:8080/auth/login" -H "accept: text/plain"') do set TOKEN=%%i

REM Create League
curl -X POST "http://localhost:8080/leagues?leagueName=NNHL" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"

REM Create Players
REM Forwards
curl -X POST "http://localhost:8080/players?email=lemieux66@pens.com&firstName=Mario&lastName=Lemieux&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=crosby87@pens.com&firstName=Sidney&lastName=Crosby&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=thegreatone@oilers.com&firstName=Wayne&lastName=Gretzky&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=golden@blues.com&firstName=Brett&lastName=Hull&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=koivu@habs.com&firstName=Saku&lastName=Koivu&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=jotoews@hawks.com&firstName=Jonathan&lastName=Toews&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=ovy8@caps.com&firstName=Alex&lastName=Ovechkin&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=viking26@habs.com&firstName=Mats&lastName=Naslund&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=stats@nordiques.com&firstName=Peter&lastName=Statsny&password=secret&position=FORWARD" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
REM Defenseman
curl -X POST "http://localhost:8080/players?email=ricky@sens.com&firstName=Erik&lastName=Karlsson&password=secret&position=DEFENSEMAN" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=larry@habs.com&firstName=Larry&lastName=Robinson&password=secret&position=DEFENSEMAN" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=uncledrew@kings.com&firstName=Drew&lastName=Doughty&password=secret&position=DEFENSEMAN" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=pk@preds.com&firstName=PK&lastName=Subban&password=secret&position=DEFENSEMAN" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
REM Goalies
curl -X POST "http://localhost:8080/players?email=onegoalie@dfd.com&firstName=Carey&lastName=Price&password=secret&position=GOALIE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=themask@dfd.com&firstName=Jacques&lastName=Plante&password=secret&position=GOALIE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=eddy@dfd.com&firstName=Ed&lastName=Belfour&password=secret&position=GOALIE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/players?email=martybro@dfd.com&firstName=Martin&lastName=Brodeur&password=secret&position=GOALIE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"

REM Subscription
curl -X POST "http://localhost:8080/leagues/1/players/2?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/3?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/4?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/5?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/6?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/7?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/8?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/9?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/10?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/11?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/12?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/13?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/14?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/15?subscription=REGULAR" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/16?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/17?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
curl -X POST "http://localhost:8080/leagues/1/players/18?subscription=SPARE" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"

REM Start new season
curl -X POST "http://localhost:8080/games/newseason?leagueId=1&startDate=2018-01-01&numberOfGames=10" -H "accept: application/json" -H "Authorization: Bearer %TOKEN%"
