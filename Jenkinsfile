pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "akhilkk03/spring-app"
        IMAGE_TAG = "v${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build & Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: 'https://index.docker.io/v1/']) {
                    sh 'mvn clean package'
                    sh "docker build -t $DOCKER_IMAGE:$IMAGE_TAG ."
                    sh "docker push $DOCKER_IMAGE:$IMAGE_TAG"
                }
            }
        }
        stage('Update Helm values.yaml') {
            steps {
                sh "sed -i 's|tag: .*|tag: $IMAGE_TAG|' helm/values.yaml"
            }
        }
        stage('Commit & Push Changes to GitHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                    sh """
                        git config --global user.name "JENKINS"
                        git config --global user.email "jenkins@ci.com"
                        git add helm/values.yaml
                        git commit -m "Updated Helm image tag to $BUILD_NUMBER"
                        git push https://$GIT_USER:$GIT_PASS@github.com/akhilkk0803/spring-app-cicd.git master
                    """
                }
            }
        }
        stage('Trigger ArgoCD Sync') {
            steps {
                sh "argocd app sync myapp"
            }
        }
    }
}
