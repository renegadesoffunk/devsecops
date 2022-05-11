pipeline {
    agent any

    stages {
        stage('Checkout Repo') {
            steps {
                checkout scm
            }
        }
        
        stage('SonarQube analysis') {
            steps {
                script {
                    dir("first-project"){
                        def scannerHome = tool 'sonar';
                        withSonarQubeEnv(installationName: 'sonar') {
                            sh """${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Simple-App \
                             -Dsonar.projectName=Simple-App \
                             -Dsonar.sources=./ \
                            """
                        }
                    }
                }
            }
        }
        
        stage('Check Sonarqube Quality Gate') {
            steps {
                script {
                    timeout(time: 1, unit: 'MINUTES') {
                        // waitForQualityGate doesn't retry after initial check
                        sleep(10)
                        
                        qualitygate = waitForQualityGate()
                        if (qualitygate.status != "OK") {
                            currentBuild.result = 'ABORTED'
                            error('Quality gate has not passed!!!')
                        }
                    }
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    dir("first-project"){
                        sh "docker build -t dashboard -f ci-cd/Dockerfile ."
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    dir("first-project"){
                        sh "docker-compose -f ci-cd/docker-compose.yml down"
                        sh "docker-compose -f ci-cd/docker-compose.yml up -d"
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                cleanWs()
            }
        }   
    }
}
