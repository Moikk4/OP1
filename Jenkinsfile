pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = 'franshk/temp-converter'
        DOCKER_TAG   = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Moikk4/OP1.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% -t %DOCKER_IMAGE%:latest ."
            }
        }

        stage('Verify Docker Image') {
            steps {
                bat "docker run --rm %DOCKER_IMAGE%:%DOCKER_TAG% --cli"
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    bat 'echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin'
                    bat "docker push %DOCKER_IMAGE%:%DOCKER_TAG%"
                    bat "docker push %DOCKER_IMAGE%:latest"
                }
            }
        }
    }

    post {
        always {
            bat 'docker logout || exit 0'
        }
    }
}
