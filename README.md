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
3. **Java 21**

## Environment Setup (Secrets)

As per requirements, our database credentials and OAuth2 Entra ID secrets are **not** committed to this repository.

1. Please locate the password-protected `.zip` file sent via email to the professor.
2. Extract the contents.
3. Place the `.env` file into the root directory of this repository before executing the pipeline. *(Note: If the `.env`
   file is missing, the Spring Boot application will fail to authenticate with the database and the Microsoft identity
   provider).*

## How to Run the Pipeline

1. Open **Git Bash** in the root directory of the repository.
2. Make the script executable by running:
   ```bash
   chmod +x pipeline.sh
3. Finally run the command:
    ```bash
    ./pipeline.sh

## What the pipeline does:

1. It "pulls the latest code" **_(just printing, not actually pulling, since we are not using something like github
   actions)_**
2. It runs static analysis **_(Checkstyle for backend)_**

   **_(Note: Build failure on violation is currently set to false, meaning
   even if there are static analysis violations, it will complete the deployment,
   and can be turned off by switching the boolean flag. This is just to prove that
   we understand how to run static analysis.)_**
3. It runs the unit tests
4. It packages the .jar file and builds the docker images
5. Deploys the local Docker prod environment
6. Finally, It runs a smoke test
   **_(Checks if 1. The spring boot server is alive (not returning 000) and that Entra ID is securing
   the page (returning HTTP 302))_**

## Viewing the Application

Once the pipeline script completes and prints the success message, the application is live on your local machine.

1. Open your web browser.
2. Navigate to: [http://localhost:8080/whether.html](http://localhost:8080/whether.html)
3. You will be immediately intercepted by Spring Security and redirected to the Microsoft Entra ID login portal to
   authenticate.
4. A valid login and password will be provided along with the secrets to the professor's email.

