pipeline {
    agent {
        kubernetes {
            yamlFile '.deployment/jenkins-agent-pod.yaml'
        }
    }
    environment {
        HOME = '.'
	NAMESPACE = "development"
        CONFIG_DIR = "${JENKINS_HOME}/configs/meals-advisor"
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
               container('jdk') {
                    withMaven {
                        sh './mvnw -DskipTests clean install'
                    }
                }
            }
        }
//	    stage('Test') {
//	        steps {
//		        sh 'mvn test'
//	        }
//	        post {
//		        always {
//		            junit '*/target/surefire-reports/*.xml'
//		        }
//	        }
//	    }
        stage('Containerize') {
            when {
                branch 'master'
            }
            steps {
		container('kaniko') {
                    dir('.deployment/scripts') {
                        sh './build-image.sh WebServer target'
                        sh './build-image.sh WebGUI dist/WebGUI'
                    }
	        }
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                container('helm') {
                    dir('.deployment/scripts') {
                        sh './deploy-app.sh'
                    }
		}
            }
        }
    }
}

