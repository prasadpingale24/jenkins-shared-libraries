def call() {

    echo "Deploying application using Docker Compose"

    sh """
        docker compose down --remove-orphans
        docker compose up -d
    """

    echo "Waiting for services to stabilize..."

    sh """
        sleep 10
        docker compose ps
    """
}
