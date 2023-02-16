@echo off
rem AUTOMAÇÃO PARA CRIAÇÃO DA IMAGEM DO BANCO DE DADOS
rem V 1.0
rem AUTOR Willian Andrade

echo ... BUILDING
echo Construindo a imagem do banco de dados
set project=isgood
set VERSION=v%1
set LOCAL_FILES=%2
set POD_NAME=rabbitmq-srv-mail
set DOCKERCOMPOSE_FILE="D:\FIAP\Fase 04\Entregavel\Docker-up.bat"

if "%VERSION%"=="" (
    echo ERRO: Nao foi informado o parametro de VERSAO
    exit
)

if "%LOCAL_FILES%"=="" (
    set LOCAL_FILES=.
)
echo INFO Versao: '%VERSION%'

SET IMAGENAME=%project%/%POD_NAME%:%VERSION%
docker build -t %IMAGENAME% %LOCAL_FILES%
echo.
docker images %IMAGENAME%
echo.

echo Image builded: %IMAGENAME%
echo.

echo.
echo Comando para subir o container:
set DOCKER_UP_CMD=docker run -d --name %POD_NAME% %IMAGENAME%
echo %DOCKER_UP_CMD%
echo.
rem call %DOCKER_UP_CMD%
echo %DOCKER_UP_CMD% >> %DOCKERCOMPOSE_FILE%
echo.

