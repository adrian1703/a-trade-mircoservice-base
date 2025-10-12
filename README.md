# a-trade-mircoservice-base

This is a base project designed to provide a runtime for plugins implementing RestApiPlugin-Interface.

## build_interface_and_template.bash

Is to be used when changing either of the dependent projects:a-trade-microservice-runtime-api or
a-trade-microservice-template change.

It rebuilds both projects, publishes the api to local maven and move the artifact of template to the test/resource
folder for testing 
