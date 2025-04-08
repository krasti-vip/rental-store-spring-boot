FROM jenkins/jenkins:2.504-jdk21

USER root

RUN apt update && apt install -y openssh-client curl

RUN curl -fsSL https://get.docker.com | sh && \
    usermod -aG docker jenkins

RUN mkdir -p /var/jenkins_home/.ssh && \
    ssh-keyscan -t ed25519 github.com >> /var/jenkins_home/.ssh/known_hosts && \
    chown -R jenkins:jenkins /var/jenkins_home/.ssh && \
    chmod 700 /var/jenkins_home/.ssh && \
    chmod 644 /var/jenkins_home/.ssh/known_hosts

RUN apt-get update && apt-get install -y maven

LABEL authors="dmitriyboyko"

EXPOSE 8080

USER jenkins

