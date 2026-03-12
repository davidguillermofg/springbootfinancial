@echo off
echo Building cliente...
cd cliente
call mvnw.cmd -q clean package -DskipTests
if errorlevel 1 (echo Build cliente failed. & cd .. & exit /b 1)
cd ..

echo Building cuenta...
cd cuenta
call mvnw.cmd -q clean package -DskipTests
if errorlevel 1 (echo Build cuenta failed. & cd .. & exit /b 1)
cd ..

echo Starting containers...
docker-compose up -d
if errorlevel 1 exit /b 1
echo Done. Cliente: http://localhost:8080  Cuenta: http://localhost:8081
