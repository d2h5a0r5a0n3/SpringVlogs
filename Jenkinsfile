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
                bat 'mvnw.cmd clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                bat 'docker-compose down'
                bat "docker-compose up --build"
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
