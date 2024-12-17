pipeline {
    agent any

    environment {
        // Database credentials
        MYSQL_ROOT_PASSWORD = 'Dharan@123'
        MYSQL_DATABASE = 'springboot'
        MYSQL_USER = 'springboot-user'
        MYSQL_PASSWORD = 'Dharan@123'

        // Docker image name
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
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh './mvnw test'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image') {
            when {
                expression { DOCKER_REGISTRY != '' }
            }
            steps {
                echo 'Tagging and pushing Docker image to registry...'
                sh "docker tag ${IMAGE_NAME}:latest ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest"
                sh "docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:latest"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying application using Docker Compose...'
                sh 'docker-compose down'
                sh 'docker-compose up -d'
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
