def call(String imageName) {

    echo "Starting Postgres test container"

    sh '''
        docker rm -f pg_test || true

        docker run -d \
            --name pg_test \
            -e POSTGRES_USER=postgres \
            -e POSTGRES_PASSWORD=postgres \
            -e POSTGRES_DB=task_manager_test \
            -p 5444:5432 \
            postgres:15-alpine
    '''

    echo "Waiting for Postgres to be ready"

    sh '''
        for i in $(seq 1 20); do
            docker exec pg_test pg_isready -U postgres && break
            echo "Waiting for Postgres... attempt $i"
            sleep 2
        done
    '''

    echo "Running backend tests"

    sh """
        docker run --rm \
            --name backend_test \
            --link pg_test:postgres \
            -e POSTGRES_HOST=postgres \
            -e POSTGRES_PORT=5432 \
            -e POSTGRES_USER=postgres \
            -e POSTGRES_PASSWORD=postgres \
            -e POSTGRES_DB=task_manager_test \
            -e SECRET_KEY=jenkins-ci-test-key \
            -e ENVIRONMENT=test \
            -e PYTHONPATH=/app \
            -v \$(pwd):/app \
            -w /app \
            python:3.13-slim \
            sh -c "pip install uv --quiet && \
                   uv sync --group dev && \
                   uv run pytest tests/ \
                        --junitxml=results.xml \
                        --cov=app \
                        --cov-report=xml \
                        -v"
    """
}
