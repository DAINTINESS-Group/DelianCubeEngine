#!bin/bash

# To be used when you want to rerun the container creation process
docker stop database-container
docker rm database-container
docker image rm delian_database
