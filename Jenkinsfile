pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
               dir ('D:/projekte/AutomationFarm/gatewayeventhub') { 
               bat 'gradlew clean build war'
           }
        }
    }
}
}