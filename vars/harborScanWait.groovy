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

    int timeoutMinutes =
        config.timeoutMinutes
            ? config.timeoutMinutes as int
            : 5

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'HARBOR_USER',
            passwordVariable: 'HARBOR_PASSWORD'
        )
    ]) {

        def client = new HarborClient(
            this,
            "https://${registry}"
        )

        timeout(time: timeoutMinutes, unit: 'MINUTES') {

            waitUntil {

                Map artifact = client.getArtifact(
                    project,
                    repository,
                    reference
                )

                String status = findScanStatus(artifact)

                echo "Harbor scan status: ${status}"

                if (status in ['Success', 'Complete', 'Finished']) {
                    echo "Harbor vulnerability scan completed."
                    return true
                }

                if (status in ['Error', 'Failed', 'Stopped']) {
                    error(
                        "Harbor vulnerability scan failed. " +
                        "Status: ${status}"
                    )
                }

                sleep 10

                return false
            }
        }
    }
}

private String findScanStatus(Map artifact) {

    Map scanOverview =
        artifact.scan_overview ?: [:]

    for (def report : scanOverview.values()) {

        if (report instanceof Map && report.scan_status) {
            return report.scan_status.toString()
        }
    }

    return 'NOT_SCANNED'
}

private void require(Map config, String name) {

    if (!config[name]) {
        error("harborScanWait: ${name} is required")
    }
}
