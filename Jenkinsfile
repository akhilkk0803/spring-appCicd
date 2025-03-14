pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "akhilkk03/spring-app"
        IMAGE_TAG = "v${BUILD_NUMBER}"
        GITHUB_REPO = "https://github.com/akhilkk0803/spring-appCicd.git"
        ARGOCD_SERVER = "localhost:8090"
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
                    bat "docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} ."
                    bat "docker push ${DOCKER_IMAGE}:${IMAGE_TAG}"
                }
            }
        }

        stage('Update Helm values.yaml') {
            steps {
                powershell '''
                    $content = Get-Content myapp/values.yaml -Raw
                    $updated = $content -replace '(?m)^(\s*?tag:\s*?).*$', "`$1${env:IMAGE_TAG}"
                    Set-Content myapp/values.yaml $updated
                '''
            }
        }

        stage('Commit & Push Changes') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
                    bat """
                        git config --global user.name "Jenkins"
                        git config --global user.email "jenkins@ci.com"
                        git checkout main
                        git add myapp/values.yaml
                        git commit -m "CI: Update image tag to ${IMAGE_TAG}"
                        git push https://%GIT_USER%:%GIT_PASS%@${GITHUB_REPO} main
                    """
                }
            }
        }
       stage('Commit & Push Changes to GitHub') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
            bat """
                git config --global user.name "JENKINS"
                git config --global user.email "jenkins@ci.com"
                git fetch origin
                git checkout master || git checkout main  # Ensure we are on the correct branch
                git reset --hard origin/master || git reset --hard origin/main
                git clean -fd
                git add myapp/values.yaml
                git commit -m "Updated Helm image tag to %BUILD_NUMBER%"
                git push https://%GIT_USER%:%GIT_PASS%@github.com/akhilkk0803/spring-appCicd.git master 
            """
        }
    }
}

        stage('Trigger ArgoCD Sync') {
    steps {
        script {
            bat """
                C:\\Windows\\System32\\argocd.exe login localhost:8090 --username admin --password WuICYQ0qjP3djeO0 --insecure
                C:\\Windows\\System32\\argocd.exe app sync spring-app
            """
        }
    }
}
    }
}
