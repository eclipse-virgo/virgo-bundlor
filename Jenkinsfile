pipeline {
  agent {
    kubernetes {
      inheritFrom 'virgo-bundlor-agent-pod'
      yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:3.5-jdk8
    command:
    - cat
    tty: true

    env:
    - name: GRADLE_USER_HOME
      value: "/tmp/gradle"
    - name: CBI_BUILD
      value: "true"

    resources:
      limits:
        memory: "6Gi"
        cpu: "2"
      requests:
        memory: "3Gi"
        cpu: "1"
"""
    }
  }
  stages {
    stage('Gradle Build') {
      steps {
        container('gradle') {
          sh 'gradle build --continue --stacktrace -x findbugsMain -x findbugsTest'
          junit '**/build/test-results/test/*.xml'
        }
      }
    }
    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
      }
    }
  }
}
