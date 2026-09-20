def call(Map config = [:]) {

    if (!config.image) {
        error("dockerPush: image is required")
    }

    if (!config.tag) {
        error("dockerPush: tag is required")
    }

    if (!config.credentials) {
        error("dockerPush: credentials is required")
    }

    String image = config.image
    String tag = config.tag
    String credentialsId = config.credentials

    String registry = image.tokenize('/')[0]

    echo "Pushing image: ${image}:${tag}"

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'REGISTRY_USER',
            passwordVariable: 'REGISTRY_PASSWORD'
        )
    ]) {

        withEnv([
            "IMAGE=${image}",
            "IMAGE_TAG=${tag}",
            "REGISTRY_HOST=${registry}"
        ]) {

            sh '''
                echo "$REGISTRY_PASSWORD" | docker login "$REGISTRY_HOST" \
                    --username "$REGISTRY_USER" \
                    --password-stdin

                docker push "$IMAGE:$IMAGE_TAG"

                docker logout "$REGISTRY_HOST"
            '''
        }
    }
}
