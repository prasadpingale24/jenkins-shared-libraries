def call(String dockerHubUser, String projectName, String imageTag) {

    def image = "${dockerHubUser}/${projectName}:${imageTag}"

    sh """
    docker build -t ${image} .
    """
}
