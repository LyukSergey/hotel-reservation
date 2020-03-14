##Docker's commands:
docker image rm [IMAGE_ID] - removing
docker image  rmi -f  [IMAGE_ID] - force removing
docker stop [CONTAINER_ID] - stop container
docker ps - list active containers
docker images - list of the docker images
docker-compose down - stop all images from docker-compose
docker-compose build - build images from docker compose
docker-compose up - run images
docker-compose up --force-recreate - run images with updating volumes
docker image push [DOCKER_USER_ID]/hotel-reservation
docker images --filter "dangling=true" - show un usage images
docker rmi $(docker images -f "dangling=true" -q) - remove all un usage images

##Test URIs
#GET All
http://localhost:8080/reservations
#GET by id
http://localhost:8080/reservations/{id}
#POST create new reservation
http://localhost:8080/reservations
#PUT update existing reservation  
http://localhost:8080/reservations
#DELETE
http://localhost:8080/reservations/{id}
#Find reservation with date range
http://localhost:8080/reservations/between?startDate=2020-02-11&endDate=2020-02-21

#test new branch

#test new branch 2