# Jenkins Shared Library

A collection of reusable Groovy scripts for Jenkins pipelines, designed to standardize and simplify the CI/CD lifecycle for containerized applications.

## 📖 How to Load the Library

To use this library in your `Jenkinsfile`, you must first define it in your Jenkins Global Configuration (**Manage Jenkins** > **System** > **Global Pipeline Libraries**).

Once configured (e.g., with the name `my-shared-library`), you can load it at the top of your `Jenkinsfile`:

```groovy
@Library('my-shared-library') _
```

---

## 🛠 Available Functions (vars/)

### 1. `prepareEnv`
Generates and validates a `.env` file for the application. It handles static variables, secure credentials, and prevents silent failures by validating for empty values.

**Usage:**
```groovy
prepareEnv(
    projectName: 'My-App',
    vars: [
        'NODE_ENV': 'production',
        'API_PORT': '8080'
    ],
    secrets: [
        [id: 'my-db-password-id', var: 'DB_PASSWORD'],
        [id: 'my-app-secret-id', var: 'APP_SECRET']
    ]
)
```

### 2. `docker_build`
Builds a Docker image using a specified name and tag.

**Usage:**
```groovy
docker_build('dockerhub-username', 'my-image-name', 'v1.0.0')
```

### 3. `docker_push`
Authenticates with Docker Hub and pushes the specified image.

**Usage:**
```groovy
docker_push('my-image-name', 'v1.0.0')
```
*Note: Expects a Jenkins credential with ID `dockerHubCred`.*

### 4. `docker_test`
Runs a test suite using `docker-compose.test.yml`. It ensures clean state by running `down` after execution.

**Usage:**
```groovy
docker_test('test-service-name')
```

### 5. `docker_deploy`
Performs a zero-downtime deployment by cycling containers via `docker compose`.

**Usage:**
```groovy
docker_deploy()
```

### 6. `healthCheck`
Politely waits for an application to respond before proceeding with the pipeline. Includes retry logic and configurable intervals.

**Usage:**
```groovy
healthCheck(
    url: 'http://localhost:8080/health',
    maxRetries: 10,
    retryInterval: 5
)
```

### 7. `clone`
A simple wrapper to clone a specific branch of a Git repository.

**Usage:**
```groovy
clone('https://github.com/user/repo.git', 'main')
```

---

## 🏗 Best Practices
1. **Config-as-Code**: Define project-specific variables in a configuration map at the top of your `Jenkinsfile` and pass them to these functions.
2. **Fail-Fast**: Use `prepareEnv` and `healthCheck` to catch configuration and runtime issues early in the pipeline.
3. **Security**: Never hardcode secrets. Always use Jenkins Credentials IDs and pass them through the `secrets` parameter in `prepareEnv`.

---

*Documentation last updated: March 2026*
