def call(Map config = [:]) {

    if (!config.host) {
        error("sshDeploy: host is required")
    }

    if (!config.user) {
        error("sshDeploy: user is required")
    }

    if (!config.port) {
        error("sshDeploy: port is required")
    }

    if (!config.credentials) {
        error("sshDeploy: credentials is required")
    }

    if (!config.command) {
        error("sshDeploy: command is required")
    }

    String host = config.host
    String user = config.user
    String port = config.port.toString()
    String credentialsId = config.credentials
    String command = config.command

    echo "Deploying to ${user}@${host}:${port}"

    sshagent(credentials: [credentialsId]) {

        sh """
            ssh -p "${port}" \
                -o StrictHostKeyChecking=no \
                "${user}@${host}" \
                '${command}'
        """
    }
}
