# a-trade-mircoservice-base

This is a base project designed to provide a runtime for plugins implementing RestApiPlugin-Interface.

## Scripts

### build_interface_and_template.bash

Is to be used when changing either of the dependent projects:a-trade-microservice-runtime-api or
a-trade-microservice-template change.

It rebuilds both projects, publishes the api to local maven and move the artifact of template to the test/resource
folder for testing

### build_base_image.bash

Automates the building of the main Docker image for the project.  
This script updates dependencies and templates, compiles the project, and uses the production Dockerfile to create a
deployable Docker image from the most current build artifacts.

### integration_test.bash

Sets up and runs integration testing inside a dedicated Docker container.

## TODO Next

1. ~~get docker up and running - make test deploy~~
2. rebuild data publisher as microservice
    1. min agg
    2. day agg
    3. derived agg hour - as in x ms
    4. derived agg topics
    5. timeframe and topics
    6. reset api
3. logging? maybe
4. get fe running
    1. be endpunkt microservice
    2. react TOO 
