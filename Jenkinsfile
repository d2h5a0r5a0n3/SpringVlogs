pipeline {
    agent any

    environment {
        MYSQL_ROOT_PASSWORD = 'Dharan@123'
        MYSQL_DATABASE = 'springboot'
        MYSQL_USER = 'springboot-user'
        MYSQL_PASSWORD = 'Dharan@123'
        IMAGE_NAME = 'springboot-app'
        DOCKER_REGISTRY = 'd2h5a0r5a0n3'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Cloning the repository...'
                checkout scm
            }
        }

        stage('Build Application') {
            steps {
                echo 'Building the Spring Boot application...'
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                bat 'mvnw.cmd test'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                bat "docker build -t %IMAGE_NAME%:latest ."
            }
        }

        stage('Push Docker Image') {
            when {
                expression { DOCKER_REGISTRY != '' }
            }
            steps {
                echo 'Tagging and pushing Docker image to registry...'
                bat "docker tag %IMAGE_NAME%:latest %DOCKER_REGISTRY%/%IMAGE_NAME%:latest"
                bat "docker push %DOCKER_REGISTRY%/%IMAGE_NAME%:latest"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying application using Docker Compose...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for details.'
        }
    }
}
