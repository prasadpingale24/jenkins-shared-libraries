def call(Map config = [:]) {
    echo "Preparing environment for ${config.projectName ?: 'project'}"
    
    // 1. Initial .env from example
    sh "cp .env.example .env"
    
    // 2. Add static variables (replacing existing ones)
    if (config.vars) {
        config.vars.each { key, value ->
            // Filter out existing key and then append new value
            sh "grep -v '^${key}=' .env > .env.tmp || true && mv .env.tmp .env"
            sh "echo '${key}=${value}' >> .env"
        }
    }

    // 3. Add secrets from credentials (replacing existing ones)
    if (config.secrets) {
        config.secrets.each { secret ->
            withCredentials([string(credentialsId: secret.id, variable: 'SECRET_VAL')]) {
                // Filter out existing key and then append new secret
                sh "grep -v '^${secret.var}=' .env > .env.tmp || true && mv .env.tmp .env"
                sh "echo ${secret.var}='${SECRET_VAL}' >> .env"
            }
        }
    }

    // 4. Validation: Check for empty values that would cause silent failures
    echo "Validating .env file for empty values..."
    def emptyVars = sh(script: "grep -E '^[^#]*=\$' .env || true", returnStdout: true).trim()
    if (emptyVars) {
        error "BUILD FAILED: The following variables are empty in .env:\n${emptyVars}\nPlease check your Jenkins Credentials or Jenkinsfile configuration."
    }
}
