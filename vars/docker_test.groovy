def call() {

    echo "Running backend tests using docker-compose.test.yml"

    try {

        sh """
        docker compose -f docker-compose.test.yml up \
            --abort-on-container-exit \
            --exit-code-from backend
        """

    } finally {

        echo "Cleaning up test containers"

        sh """
        docker compose -f docker-compose.test.yml down -v
        """
    }
}
