pipeline {
    agent {
        dockerfile {
			args '-v /opt/tomcat/.jenkins/workspace/geh:/opt -w /opt'	
		}			
    }
	environment {
        HOME = '.'
    }
    stages {
        stage('Test') {
            steps {
                sh 'pwd'
		sh './gradlew clean build -x test war'		
            }
        }
    }
}}
