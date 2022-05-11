SUMMARY
##The following repo contains a vuejs application and an orchestration which brings up jenkins and sonarqube. The sequence which should be followed is simple - you bring up jenkins and sonarqube containers via docker-compose, then you navigate to the jenkins ui from which you trigger a pipeline/job (which was automatically populated and all is "as a code"). The pipeline basically checks out this very repo, invokes the sonarqube to scan the vuejs app and if the quality gate passes, the app will be build and deployed (via Dockerfile & docker-compose).

##Getting started
##The assumption here is that the machine will runing a Linux-like Operating system with docker/docker-compose installed on it and a systemd in place.
##In order to run some of the requirements of the SonarQube the following parameters have to be executed on the host machine.
sudo sysctl -w vm.max_map_count=524288  
sudo sysctl -w fs.file-max=131072  

#That one is not ok from security perspective but as it is an example project we take advantage of it. The reason for applying those permissions is so the jenkins can build/deploy the VueJS app on tost machine
sudo chmod 666 /var/run/docker.sock  



##First we have to spin up the jenkins and sonarqube (sonarqube has a db and few other builtin capabilities which are all handled by the docker compose file in the root context of the repo)

## This will build the jenkins image with a specific pipeline/job which will be executed at a later stage
docker-compose build
## This will spin up the already built jenkins image along with sonarqube and its components
docker-compose up -d 


## The jenkins and sonarqube are supposed ot be fully initialized after ~60 seconds (assuming the docker images are downloaded). The jenkins is populated by a pipeline/job which is 

Log in to the jenkins UI on http://localhost:8099
The pipeline can be started by navigating within the only job available and pressing "Build Now" button on the interface.




## Security Hotspot place in first-project/src/main.js
const crypto = require("crypto");
const hash = crypto.createHash('sha1');
