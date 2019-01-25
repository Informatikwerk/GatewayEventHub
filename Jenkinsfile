pipeline {
    agent none
    stages {
        stage('Build') {
            agent {
                dockerfile {
                        args '-v /opt/tomcat/.jenkins/workspace/gatewayeventhub:/opt -w /opt'
                }
            }
             environment {
                HOME = '.'
            }
            steps {
                sh 'pwd'
                sh './gradlew clean build -x test war'
            }
        }
        stage('Deploy') {
            agent any
            steps {
                sh 'GEH_HOME=$PWD'
                sh 'echo Calling ------------- $GEH_HOME/gradlew bootRepackage -Pprod buildDocker'
                sh './gradlew bootRepackage -Pprod buildDocker --stacktrace'
                sh 'echo Calling ------------- $GEH_HOME/docker-compose -f src/main/docker/app.yml up -d'
                sh 'docker-compose -f src/main/docker/app.yml up -d'
            }
        }
    }

}
