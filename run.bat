@echo off
echo Building NGOConnect JavaFX...
call mvn -q package -DskipTests
if %errorlevel% neq 0 (
    echo Build failed. Make sure Maven and Java 17+ are installed.
    pause
    exit /b 1
)
echo Launching NGOConnect...
mvn javafx:run
