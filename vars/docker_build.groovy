def call(String dockerHubUser, String imageName, String imageTag) {
    def fullImage = "${dockerHubUser}/${imageName}:${imageTag}"
    echo "Building image: ${fullImage}"
    sh "docker build -t ${fullImage} ."
}
