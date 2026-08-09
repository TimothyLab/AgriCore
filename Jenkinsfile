pipeline {
    agent none

    stages {

        stage('Start BDD') {
            agent any
            steps {
                sh 'docker compose up -d agricore-mysql'
            }
        }

        stage('Build and Test') {
            agent { 
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                    args '-v /root/.m2:/root/.m2 --network devops'
                }
            }

            steps {
                dir('agricore_spring_boot') {
                    sh '''
                        mvn clean verify \
                        -Dspring.datasource.url=jdbc:mysql://agricore-mysql:3306/agricore \
                        -Dspring.datasource.username=root \
                        -Dspring.datasource.password=root
                    '''
                }
            }
        }

        stage('Sonar analysis') {
            agent {
                docker {
                    image 'maven:3.9.11-eclipse-temurin-21'
                    args '-v /root/.m2:/root/.m2 --network devops'
                }
            }

            steps {
                withSonarQubeEnv('sonar') {
                    dir('agricore_spring_boot') {
                        sh '''
                            mvn sonar:sonar \
                            -Dsonar.projectKey=agricore \
                            -Dsonar.projectName=AgriCore
                        '''
                    }
                }
            }
        }

        stage('Stop database') {
            agent any
            steps {
                sh 'docker compose stop agricore-mysql'
            }
            
        }
        
    }
}