# Whether-App-

Rules:
!!!Always use "git pull origin main" before creating a PR!!!

Cloning Repo:
git clone https://github.com/lukestafford1/Whether-App-

Opening locally: cd Whether-App-
code -r .

Running Spring boot server for API testing:
cd backend
./mvnw spring-boot:run

# CI/CD Pipeline & Deployment Guide

This guide outlines how to execute our deployment pipeline. We have consolidated the build, test, and deployment process
into a single, one-touch execution script (`pipeline.sh`).

## Prerequisites

Before running the pipeline, ensure the following is running:

1. **Docker Desktop** (Must be open and running in the background)
2. **Git Bash** (Recommended for Windows execution)

## Environment Setup (Secrets)

As per requirements, our database credentials and OAuth2 Entra ID secrets are **not** committed to this repository.

1. Please locate the password-protected `.zip` file sent via email to the professor.
2. Extract the contents.
3. Place the `.env` file into the root directory of this repository before executing the pipeline.

## How to Run the Pipeline

1. Open **Git Bash** in the root directory of the repository.
2. Make the script executable by running:
   ```bash
   chmod +x pipeline.sh
3. Finally run the command:
    ```bash
    ./pipeline.sh

## What the pipeline does:

1. **Pulls the latest code:** *(just printing, not actually pulling, simulating a CI/CD runner)*
2. **Multi-Stage Docker Build Starts**
3. **Isolated Testing:** Runs static analysis (Checkstyle) and Unit Tests **inside** the Docker build
   environment.

*(Note: Build failure on violation is currently set to false, meaning even if there are static analysis violations, it
will complete the deployment. This proves we understand how to integrate static analysis into containerized builds.)*

4. **Packaging:** Packages the `.jar` file and builds the lightweight production Docker image.
5. **Deployment:** Deploys the local Docker production environment (Spring Boot + MySQL).
6. **Smoke Test:** Runs a check to verify the Spring Boot server is alive and that Entra ID is
   securing the page (expecting HTTP 302 or HTTP 200).

## Troubleshooting

If you experience database authentication errors (`Access denied`), port conflicts, or the pipeline seems to be using
old cached code, run these commands in Git Bash to force a clean build:

```bash
# 1. Stop containers and destroy the corrupted database volume
docker-compose down -v

# 2. Clear out old cached build layers (Type 'y' to confirm)
docker system prune -a --volumes

# 3. Re-run the pipeline
./pipeline.sh
```

## Viewing the Application

Once the pipeline script completes and prints the success message, the application is live on your local machine.

1. Open your web browser.
2. Navigate to: [http://localhost:8080/whether.html](http://localhost:8080/whether.html)
3. You will be immediately intercepted by Spring Security and redirected to the Microsoft Entra ID login portal to
   authenticate.
4. A valid login and password has been provided along with the secrets to the professor's email.
5. The website currently displays weather data about a city searched by a user using the search bar, the search bar currently only accepts single strings when searching for a city (although, cities like Los Angeles and San Diego do work), searching for Bothell, Washington or PyongYang, North Korea will result in error.
6. The map feature shows the location of the searched city and the corresponding data appears via a window placed on a pin to the city center. If the user clicks on the map to place a pin, it will not show weather data of any location, only coordinates of the placed pin.

