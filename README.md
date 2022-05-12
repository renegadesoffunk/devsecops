## Summary
The following repo contains a vuejs application (which I previously created for few minutes following ) and an orchestration which brings up jenkins and sonarqube. The sequence which should be followed is simple - you bring up jenkins and sonarqube containers via docker-compose, then you navigate to the jenkins ui from which you trigger a pipeline/job (which was automatically populated and all is "as a code"). The pipeline basically checks out this very repo, invokes the sonarqube to scan the vuejs app and if the quality gate passes, the app will be build and deployed (via Dockerfile & docker-compose).

## Assumptions
All of the execution steps below were successfully testes on Ubuntu 20 LTS, CentOS 7, ParrotOS 5. Linux-like distribution with systemd should handle everything smoothly. The machine/host on which all of the actions are going to take place needs to have available sufficient resources (8GB RAM, 15GB free space). 
In terms of systemd, the following changes need to be made on the host machine (because of the sonarqube's builtin elasticsearch) prior the exetution steps.  
  `sudo sysctl -w vm.max_map_count=524288`  
  `sudo sysctl -w fs.file-max=131072`  
And one more steps which is changing the permissions of /var/run/docker.sock in order to facilitate the jenkins container's capability to talk to the docker api on the host machine and brings up the vuejs sample app if the pipeline is invoked and passes the quality gate. The required command is:  
  `sudo chmod 666 /var/run/docker.sock`  
Honorable mention: those permissions will be overwritten to their default state if the host is rebooted.

## Execution Steps
First we have to spin up the jenkins and sonarqube (sonarqube has a db and few other builtin capabilities which are all handled by the docker compose file in the root context of the repo)
This will build the jenkins image with a specific pipeline/job which will be executed at a later stage.  
  `docker-compose build`  
Afterwards the following line is in turn. That one will spin up the already built jenkins image along with sonarqube and its components:  
  `docker-compose up -d`  
The jenkins and sonarqube are supposed to be fully initialized after ~1-2 minutes (assuming the docker images are previously downloaded). You can check them up by navigating to their UIs. Their http interfaces map to 0.0.0.0:{some port described in the docker-compose file} 

You may continue now with going to the jenkins UI on http://localhost:8099 (or any other of the host's interfaces IP addresses with the same port)
The pipeline can be started by navigating within the only job available and pressing "Build Now" button on the interface.
Once the job is successfully finished you should have the app accessible on http://localhost:9988 (or any other of the host's interfaces IP addresses with the same port)
The quality gate is passing because the sample app was made quite recently.

Now, in order to fail the quality gate, we have to submit a code which has a "low security index"

You should put the lines below in file "jenkins/jobs/build-job/main.js" on line 10:  
`const crypto = require("crypto");`   
`const hash = crypto.createHash('sha1');`  
That one however has to be pushed to the repo as the jenkins checks out the repo on which the sonarqube will be execute the scanning. So in order to facilitate this stage we have 2 options:
1. You tell me your github accounts and I provide you with permissions to push to this repo, so you can test it.
2. You fork this repo and test it. If you go with this option you should adjust the repo URL within file "jenkins/jobs/build-job/config.xml"
This is the beauty of Jenkins - it will check out a repo accessible over http in its own workspace :)
