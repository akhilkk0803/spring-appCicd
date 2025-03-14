pipeline{
    agent any
    environment{
        DOCKER_IMAGE="akhilkk03/spring-app"
        IMAGE_TAG="v${BUILD_NUMBER}"
    }
    stages{
        stage('Checkout scm'){
            steps{
                checkout scm
            }
        }
        stage('Build & Push docker image'){
            steps{
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh 'mvn clean package'
                sh "docker build -t $DOCKER_IMAGE:$IMAGE_TAG ."
                sh "docker push $DOCKER_IMAGE:$IMAGE_TAG"
            }
        }
    }
    stage('Update helm and deploy'){
        steps{
            sh "helm upgrade --install myapp ./myapp --set image.tag=$IMAGE_TAG"
        }
    }
    stage("Argocd sync"){
        steps{
            sh "argocd app sync myapp"
        }
    }

}