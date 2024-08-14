# SPRING EXAMPLE 2

Sample maven project for spring boot applications using java and kotlin languages.
It follows [hexagonal architecture principles](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)
and uses different testing techniques. It shows usages of other common technologies (like NoSQL database).

## How to build

There are two options:

- Build locally running `mvn clean package`.
- Build inside a docker container (basically you don't even need Maven installed locally)
  by using the .sh script typing: `./build.sh`. You will find the built file in the
  usual maven directories.

### CI/CD

Linked to this project there is a CI/CD integration (using Github actions) that is triggered
on every master push. This will push automatically the docker image on the dockerhub registry and Amazon public ECR,
ready for the deployment.

## Run the entire application

Run using `./run.sh` command inside the `deploy` directory.
This will download from dockerhub the images and run everything using docker compose.\
You can stop shut everything down running the script `./stop.sh`.

### AWS

This application was deployed in my personal AWS cluster. In order to do that, run an EC2 instance, install Docker
and run the docker image pushed on the ECR.

### Raspberry PI

This application was successfully deployed in a Raspberry PI 4 Model B, with the only fix that mongo db must be 
version 4.4.18 or earlier, for some incompatibilities between latest mongo db versions and ARM architecture. To run on
Raspberry, just enter the deploy folder and run the `./run.sh` script. Everything should run smoothly.

## Local testing

If you want to test the app locally, first run the script `./run-local-environment.sh`. This starts a 
mongodb instance and a mongo express interface reachable at `localhost:8081`. 
Then run the application.
