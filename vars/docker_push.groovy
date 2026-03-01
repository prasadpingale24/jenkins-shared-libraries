def call(String project, String imageTag) {

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerHubCred',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {

        sh """
        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
        docker tag ${project}:${imageTag} ${DOCKER_USER}/${project}:${imageTag}
        docker push ${DOCKER_USER}/${project}:${imageTag}
        """
    }
}
