import com.pspworks.cicd.HarborClient

def call(Map config = [:]) {

    require(config, 'registry')
    require(config, 'project')
    require(config, 'repository')
    require(config, 'reference')
    require(config, 'credentials')

    String registry = config.registry
    String project = config.project
    String repository = config.repository
    String reference = config.reference
    String credentialsId = config.credentials

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'HARBOR_USER',
            passwordVariable: 'HARBOR_PASSWORD'
        )
    ]) {

        def client = new HarborClient(
            this,
            "https://${registry}",
            env.HARBOR_USER,
            env.HARBOR_PASSWORD
        )

        int status = client.triggerScan(
            project,
            repository,
            reference
        )

        echo "Harbor scan request HTTP status: ${status}"

        if (!(status in [202, 409])) {
            error(
                "Harbor scan request failed. " +
                "Expected HTTP 202 or 409, got ${status}"
            )
        }

        if (status == 202) {
            echo "Harbor accepted the scan request."
        } else {
            echo "Harbor reports that a scan is already queued or running."
        }
    }
}

private void require(Map config, String name) {

    if (!config[name]) {
        error("harborScan: ${name} is required")
    }
}
