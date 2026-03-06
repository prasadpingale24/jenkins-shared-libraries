def call(Map config = [:]) {
    echo "Preparing environment for ${config.projectName ?: 'project'}"
    
    // 1. Initial .env from example
    sh "cp .env.example .env"
    
    // 2. Add static variables
    if (config.vars) {
        config.vars.each { key, value ->
            sh "echo '${key}=${value}' >> .env"
        }
    }

    // 3. Add secrets from credentials
    if (config.secrets) {
        config.secrets.each { secret ->
            // config.secrets should be a list of maps: [[id: 'cred-id', var: 'ENV_VAR_NAME']]
            withCredentials([string(credentialsId: secret.id, variable: 'SECRET_VAL')]) {
                // Use \$ to escape it from Groovy interpolation, but leave $ for the shell to interpolate
                // We wrap it in single quotes in the echo command to handle special characters carefully
                sh "echo '${secret.var}=\$SECRET_VAL' >> .env"
            }
        }
    }
}
