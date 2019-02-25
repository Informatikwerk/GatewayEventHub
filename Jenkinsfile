pipeline {
    agent none
	environment {
        SSH_IP = 'jenkins@157.97.108.196'
        SSH_PORT = '2807'
		REGISTRY = 'localhost:5000'
    }
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
            agent { label 'master' }
            steps {
                sh 'GEH_HOME=$PWD'
                sh 'echo Calling ------------- $GEH_HOME/gradlew bootRepackage -Pprod buildDocker'
                sh './gradlew bootRepackage -Pprod buildDocker --stacktrace'
                sh 'echo Calling ------------- $GEH_HOME/docker-compose -f src/main/docker/app.yml up -d'
                sh 'docker-compose -f src/main/docker/app.yml up -d --remove-orphans'
            }
        }
		stage('Remote Build') {
			agent { label 'master' }
			steps {
				sshagent (['b857f680-137f-4664-8478-c76098a49af7']) {
					sh 'scp -r -P ${SSH_PORT} /opt/tomcat/.jenkins/workspace/gatewayeventhub/backup_db.sh ${SSH_IP}:/media/app/automation/gatewayeventhub/backup_db.sh'
					sh 'ssh -p ${SSH_PORT} -T -R 5000:${REGISTRY} ${SSH_IP} sh /media/app/automation/gatewayeventhub/backup_db.sh'
                    sh 'docker image tag gatewayeventhub ${REGISTRY}/gatewayeventhub'
					sh 'docker push ${REGISTRY}/gatewayeventhub'
					sh 'scp -r -P ${SSH_PORT} /opt/tomcat/.jenkins/workspace/gatewayeventhub/src/main/docker/app.yml ${SSH_IP}:/media/app/automation/gatewayeventhub/app.yml'
					sh 'scp -r -P ${SSH_PORT} /opt/tomcat/.jenkins/workspace/gatewayeventhub/src/main/docker/mysql.yml ${SSH_IP}:/media/app/automation/gatewayeventhub/mysql.yml'
					sh 'ssh -p ${SSH_PORT} -T -R 5000:${REGISTRY} ${SSH_IP} docker pull ${REGISTRY}/gatewayeventhub'
					sh 'ssh -p ${SSH_PORT} -T -R 5000:${REGISTRY} ${SSH_IP} docker-compose -f /media/app/automation/gatewayeventhub/app.yml up -d'
				}
			}
		}
    }
}
