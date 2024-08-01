#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Print a message to indicate that the script is starting.
echo "Starting Fineract..."

# Optionally, you can perform additional setup here.
# For example, you might want to run database migrations, check for required files, etc.

# Run the Fineract application using the Java command.
# Adjust the classpath and main class as needed for your application.
java -jar fineract-provider.jar

# Optionally, you can handle signals here if you need to.
# For example, you might trap termination signals to clean up resources.
