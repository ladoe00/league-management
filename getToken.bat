@echo off

SET PASSWORD=%1

for /f %%i in ('curl -s -u admin:%PASSWORD% -X GET "http://localhost:8080/auth/login" -H "accept: text/plain"') do set TOKEN=%%i

echo %TOKEN%