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
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: 'https://index.docker.io/v1/']) ([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh 'mvn clean package'
                sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                sh "docker build -t $DOCKER_IMAGE:$IMAGE_TAG ."
                sh "docker push $DOCKER_IMAGE:$IMAGE_TAG"
            }
        }
    }
    stage('Update helm and deploy'){
        steps{
            def valuesfile="helm/values.yaml"
            sh "sed -i 's|tag: .*|tag:$IMAGE_TAG|'$valuesfile"
        }
    }
    stage('Commit & Push Changes to github'){
        steps{
 withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')] {
    sh """
        git config --global user.name "JENKINS"
        git config --global user.email "jenkins@ci.com"
        git add helm/values.yaml
        git commit -m "updated heml image tag to $BUILD_NUMBER"
        git push https://$GIT_USER:$GIT_PASS@github.com/akhilkk0803/spring-app-cicd.git master
    """
 }
     }
    }
    stage("Argocd sync"){
        steps{
            sh "argocd app sync myapp"
        }
    }

}