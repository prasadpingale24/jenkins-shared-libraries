def call(String imageName, String imageTag) {
    withCredentials([
        usernamePassword(
            credentialsId: 'dockerHubCred',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {
        def fullImage = "${DOCKER_USER}/${imageName}:${imageTag}"
        sh """
        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
        docker push ${fullImage}
        """
    }
}
