// CI for demo-app. Runs on the isolated "build" agent (no Docker socket).
// Images are built by the remote rootless BuildKit daemon over mTLS.
pipeline {
  agent { label 'build' }

  options {
    timeout(time: 30, unit: 'MINUTES')
    disableConcurrentBuilds(abortPrevious: true)
    buildDiscarder(logRotator(numToKeepStr: '20'))
    timestamps()
  }

  environment {
    IMAGE_REPO    = 'registry:5000/demo-app'
    BUILDKIT_ADDR = 'tcp://buildkitd:1234'
    BUILDKIT_TLS  = '--tlsservername buildkitd --tlscacert /run/secrets/buildkit/ca.pem --tlscert /run/secrets/buildkit/client.pem --tlskey /run/secrets/buildkit/client-key.pem'
  }

  stages {
    stage('Build & test') {
      steps {
        sh './mvnw -B -ntp verify'
      }
      post {
        always {
          junit allowEmptyResults: false, testResults: 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('SonarQube analysis + quality gate') {
      steps {
        withSonarQubeEnv('sonarqube') {
          // The scanner polls SonarQube and fails the build if the quality gate fails.
          sh './mvnw -B -ntp sonar:sonar -Dsonar.qualitygate.wait=true -Dsonar.qualitygate.timeout=300'
        }
      }
    }

    stage('Container image') {
      environment {
        // Only main publishes images; pull requests build without pushing.
        PUSH = "${env.BRANCH_NAME == 'main' ? 'true' : 'false'}"
      }
      steps {
        sh '''
          buildctl --addr "$BUILDKIT_ADDR" $BUILDKIT_TLS build \
            --frontend dockerfile.v0 --local context=. --local dockerfile=. \
            --output "type=image,name=$IMAGE_REPO:$GIT_COMMIT,push=$PUSH" \
            --metadata-file image-metadata.json --progress=plain
        '''
        script {
          def digest = readJSON(file: 'image-metadata.json')['containerimage.digest']
          if (!(digest ==~ /sha256:[0-9a-f]{64}/)) {
            error("Unexpected image digest: ${digest}")
          }
          writeFile file: 'image-ref.txt', text: "${env.IMAGE_REPO}@${digest}\n"
          currentBuild.description = "${env.PUSH == 'true' ? 'pushed' : 'built'} ${digest.take(19)}"
        }
        archiveArtifacts artifacts: 'image-ref.txt, image-metadata.json', fingerprint: true
      }
    }
  }
}
