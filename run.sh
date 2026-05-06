#!/bin/bash
echo "Building NGOConnect JavaFX..."
mvn -q package -DskipTests
if [ $? -ne 0 ]; then
    echo "Build failed. Ensure Maven 3.6+ and Java 17+ are installed."
    exit 1
fi
echo "Launching NGOConnect..."
mvn javafx:run
