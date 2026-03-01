def call(String project, String imageTag) {

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerHubCred',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {

        sh """
        docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
        docker tag ${project}:${imageTag} ${DOCKER_USER}/${project}:${imageTag}
        docker push ${DOCKER_USER}/${project}:${imageTag}
        """
    }
}
