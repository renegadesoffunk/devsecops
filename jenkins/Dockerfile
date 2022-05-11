FROM jenkins/jenkins:2.332.3-jdk11
USER root

# Update OS and install Docker
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli docker-compose nodejs

# Disable Jenkins Initial Setup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
WORKDIR /var/jenkins_home

# Copy Required Files
COPY --chown=jenkins ./plugins/plugins.yml .
COPY --chown=jenkins ./config/jenkins.yml .
COPY --chown=jenkins jobs jobs

RUN groupadd docker && usermod -aG docker jenkins

# Install Jenkins Plugin Manager
USER jenkins
RUN jenkins-plugin-cli --plugin-file plugins.yml