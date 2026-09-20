package com.pspworks.cicd

class HarborClient implements Serializable {

    private final def steps
    private final String baseUrl
    private final String username
    private final String password

    HarborClient(
        def steps,
        String baseUrl,
        String username,
        String password
    ) {
        this.steps = steps
        this.baseUrl = baseUrl
        this.username = username
        this.password = password
    }

    int triggerScan(
        String project,
        String repository,
        String reference
    ) {

        String url =
            "${baseUrl}/api/v2.0/projects/${project}/repositories/${repository}/artifacts/${reference}/scan"

        return steps.sh(
            script: """
                curl -sk \
                    --user "\$HARBOR_USER:\$HARBOR_PASSWORD" \
                    -X POST \
                    -o /dev/null \
                    -w "%{http_code}" \
                    "${url}"
            """,
            returnStdout: true
        ).trim().toInteger()
    }

    Map getArtifact(
        String project,
        String repository,
        String reference
    ) {

        String url =
            "${baseUrl}/api/v2.0/projects/${project}/repositories/${repository}/artifacts/${reference}?with_scan_overview=true"

        String json = steps.sh(
            script: """
                curl -fsSk \
                    --user "\$HARBOR_USER:\$HARBOR_PASSWORD" \
                    -H "X-Accept-Vulnerabilities: application/vnd.security.vulnerability.report; version=1.1" \
                    "${url}"
            """,
            returnStdout: true
        ).trim()

        return steps.readJSON(text: json)
    }

    Map getVulnerabilityReport(
        String project,
        String repository,
        String reference
    ) {

        String url =
            "${baseUrl}/api/v2.0/projects/${project}/repositories/${repository}/artifacts/${reference}/additions/vulnerabilities"

        String json = steps.sh(
            script: """
                curl -fsSk \
                    --user "\$HARBOR_USER:\$HARBOR_PASSWORD" \
                    -H "X-Accept-Vulnerabilities: application/vnd.security.vulnerability.report; version=1.1" \
                    "${url}"
            """,
            returnStdout: true
        ).trim()

        return steps.readJSON(text: json)
    }
}
