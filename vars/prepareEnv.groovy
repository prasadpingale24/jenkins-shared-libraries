def call() {
        withCredentials([
            string(credentialsId: 'taskManagerBackendSecretKey', variable: 'SECRET_KEY'),
            string(credentialsId: 'taskManagerBackendPassword', variable: 'POSTGRES_PASSWORD')
        ]) {

sh """
cat > .env <<EOF
POSTGRES_USER=postgres
POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
POSTGRES_DB=task_manager
POSTGRES_HOST=db
POSTGRES_PORT=5432

SECRET_KEY=${SECRET_KEY}
ALGORITHM=HS256
ACCESS_TOKEN_EXPIRE_MINUTES=1440
BACKEND_CORS_ORIGINS=["http://localhost:3000", "http://72.60.78.85:3000"]
PROJECT_NAME=Team Tasks Manager
BACKEND_PORT=8000
EOF
"""
        }
}
