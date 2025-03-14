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
                    bat 'mvn clean package'
                    bat "docker build -t %DOCKER_IMAGE%:%IMAGE_TAG% ."
                    bat "docker push %DOCKER_IMAGE%:%IMAGE_TAG%"
                }
            }
        }
        stage('Update Helm values.yaml') {
            steps {
                bat 'powershell -Command "(Get-Content myapp/values.yaml) -replace \'tag: .*\', \'tag: %IMAGE_TAG%\' | Set-Content myapp/values.yaml"'
            }
        }
        stage('Commit & Push Changes to GitHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                    bat """
                        git fetch --all
                        git checkout master
                        git reset --hard origin/master
                        git pull origin master --rebase
                        git config --global user.name "JENKINS"
                        git config --global user.email "jenkins@ci.com"
                        git add myapp/values.yaml
                        git commit -m "Updated Helm image tag to %BUILD_NUMBER%"
                        git push https://%GIT_USER%:%GIT_PASS%@github.com/akhilkk0803/spring-app-cicd.git master
                    """
                }
            }
        }
        stage('Trigger ArgoCD Sync') {
            steps {
                bat "C:\\Windows\\System32\\argocd.exe sync myapp"
            }
        }
    }
}
