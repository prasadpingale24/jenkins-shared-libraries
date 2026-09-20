import com.pspworks.cicd.HarborClient
import com.pspworks.cicd.VulnerabilityReport

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
            "https://${registry}"
        )

        Map rawReport = client.getVulnerabilityReport(
            project,
            repository,
            reference
        )

        return new VulnerabilityReport(rawReport)
    }
}

private void require(Map config, String name) {

    if (!config[name]) {
        error("harborReport: ${name} is required")
    }
}
