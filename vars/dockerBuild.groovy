def call(Map config = [:]) {

    if (!config.image) {
        error("dockerBuild: image is required")
    }

    if (!config.tag) {
        error("dockerBuild: tag is required")
    }

    String image = config.image
    String tag = config.tag

    echo "Building image: ${image}:${tag}"

    sh """
        docker build \
            -t "${image}:${tag}" \
            .
    """
}
