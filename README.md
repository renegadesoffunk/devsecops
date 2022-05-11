## SUMMARY
The following repo contains a vuejs application and an orchestration which brings up jenkins and sonarqube. The sequence which should be followed is simple - you bring up jenkins and sonarqube containers via docker-compose, then you navigate to the jenkins ui from which you trigger a pipeline/job (which was automatically populated and all is "as a code"). The pipeline basically checks out this very repo, invokes the sonarqube to scan the vuejs app and if the quality gate passes, the app will be build and deployed (via Dockerfile & docker-compose).

## Assumptions
All of the execution steps below were successfully testes on Ubuntu 20 LTS, CentOS 7, ParrotOS 5. Linux-like distribution with systemd should handle everything smoothly. The machine/host on which all of the actions are going to take place needs to have available sufficient resources (8GB RAM, 15GB free space). 
In terms of systemd, the following changes need to be made on the host machine (because of the sonarqube's builtin elasticsearch) prior the exetution steps.
### sudo sysctl -w vm.max_map_count=524288  
### sudo sysctl -w fs.file-max=131072  
And one more steps which is changing the permissions of /var/run/docker.sock in order to facilitate the jenkins container's capability to talk to the docker api on the host machine and brings up the vuejs sample app if the pipeline is invoked and passes the quality gate. The required command is:
sudo chmod 666 /var/run/docker.sock
Honorable mention: those permissions will be overwritten to their default state if the host is rebooted.

## Execution Steps
First we have to spin up the jenkins and sonarqube (sonarqube has a db and few other builtin capabilities which are all handled by the docker compose file in the root context of the repo)
This will build the jenkins image with a specific pipeline/job which will be executed at a later stage
### docker-compose build
## This will spin up the already built jenkins image along with sonarqube and its components
### docker-compose up -d 


## The jenkins and sonarqube are supposed ot be fully initialized after ~60 seconds (assuming the docker images are downloaded). The jenkins is populated by a pipeline/job which is 

Log in to the jenkins UI on http://localhost:8099
The pipeline can be started by navigating within the only job available and pressing "Build Now" button on the interface.




## Security Hotspot place in first-project/src/main.js
const crypto = require("crypto");
const hash = crypto.createHash('sha1');
