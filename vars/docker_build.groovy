def call(String projectName, String imageTag, String dockerHubUser) {

    def image = "${dockerHubUser}/${projectName}:${imageTag}"

    sh """
    docker build -t ${image} .
    """
}
