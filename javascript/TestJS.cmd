@echo off

if "%~2" == "" (
    echo Usage: %~n0 TEST-CLASS MODE VARIANT?
    exit /b 1
)

set "OUT=__OUT"
set "CLASS=%~1"
set "ARGS=%~2 %~3"

set "DIR=%~dp0"
set "DIR=%DIR:~0,-1%"
set "LIB=%DIR%/graal/*"

if exist "%OUT%" rmdir /s /q "%OUT%"

javac ^
    -encoding utf-8 ^
    -d "%OUT%" ^
    "--class-path=%LIB%;%DIR%/../common;%DIR%" ^
    "%DIR%/%CLASS:.=/%.java" ^
  && java -ea "--module-path=%LIB:~0,-2%" "--class-path=%OUT%" "%CLASS%" %ARGS%
