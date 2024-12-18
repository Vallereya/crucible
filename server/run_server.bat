@echo off
cd Server
echo Compiling server...
crystal build server.cr -o server.exe

if %errorlevel% neq 0 (
    echo Compilation failed. Please check for errors and try again.
    pause
    exit /b %errorlevel%
)
echo Compilation successful. Running server...
server.exe
pause