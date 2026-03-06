def call(String serviceName = 'backend') {
    echo "Running tests for service: ${serviceName} using docker-compose.test.yml"
    try {
        sh """
        docker compose -f docker-compose.test.yml up \
            --abort-on-container-exit \
            --exit-code-from ${serviceName}
        """
    } finally {
        echo "Cleaning up test containers"
        sh "docker compose -f docker-compose.test.yml down -v"
    }
}
