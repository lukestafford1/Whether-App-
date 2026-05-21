#!/bin/bash

echo "Starting Weather App CI/CD Pipeline:"
#Pull the latest code
echo "----------------------------------------"
echo "Pulling latest code from main"
# git pull origin main - Not really cause we aren't using something like github actions
echo "Code pulled successfully."

#Package Image & Deploy to Prod
echo "----------------------------------------"
echo "Packaging Docker Images and Deploying to Production"
echo "Note: The build continues even if static checks fail (can be changed by a simple flag toggle)"


docker-compose up -d --build
if [ $? -ne 0 ]; then
    echo "Docker build failed. Static analysis or Unit Tests may have failed. NOT PROCEEDING."
    exit 1
fi
echo "Containers built and deployed."

#Smoke Test
echo "----------------------------------------"
echo "Running Smoke Test to verify deployment"
echo "Waiting for Spring Boot and MySQL to initialize (this may take up to 60 seconds on a cold start)..."

#Check every 2 seconds, up to 30 times (60 seconds total)
for i in {1..30}; do
    HTTP_STATUS=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost:8080/whether.html)

    if [ "$HTTP_STATUS" -eq 302 ] || [ "$HTTP_STATUS" -eq 200 ]; then
        echo "Smoke Test Passed! System responded with status: $HTTP_STATUS"
        echo "CI/CD Pipeline executed successfully. Application is LIVE."
        exit 0
    fi

    # Wait 2 seconds before checking again
    sleep 2
done

# If the loop finishes 60 seconds and still hasn't succeeded:
echo "Deployment Verification failed. HTTP Status: $HTTP_STATUS"
docker logs weather_backend
exit 1