# SPRING EXAMPLE 2

Sample maven project for spring boot applications using java and kotlin languages.
It
follows [hexagonal architecture principles](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)
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

## How to run

If you want to test the app locally, first run the script `./run-local-environment.sh`. This starts a
mongodb instance reachable at `localhost:27017`, then run the application.  
There is also a script `./run.sh` inside the `deploy` folder.
This will download from dockerhub the images and run everything using docker compose.You can stop shut everything down running the script `./stop.sh`.

The application contains two configuration files, one for local testing (`application.yml`) 
and the other for production deployment (`application-prod.yml`). Inside them, among configuration
variables, there is the field `enabledDB` that is used to specify which database you want to use. You can
choose between `mongo` and `dynamo`.

## Deploy

### AWS

This application was deployed in my personal AWS cluster. In order to do that, run an EC2 instance, install Docker
and run the docker image pushed on the ECR. AWS **does not** leverage any file inside the `deploy` folder.

### Raspberry PI

This application was successfully deployed in a Raspberry PI 4 Model B, with the only fix that mongo db must be
version 4.4.18 or earlier, for some incompatibilities between latest mongo db versions and ARM architecture. To run on
Raspberry, just enter the `deploy` folder and run the `./run.sh` script. Everything should run smoothly.

## Test resources

In the `http` folder you can find http sample request.