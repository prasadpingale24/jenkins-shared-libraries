package com.pspworks.cicd

class SecurityPolicy implements Serializable {

    private final Map configuration

    SecurityPolicy(Map configuration = [:]) {
        this.configuration = configuration ?: [:]
    }

    void evaluate(VulnerabilityReport report) {

        int critical = report.critical()
        int high = report.high()
        int medium = report.medium()
        int low = report.low()
        int unknown = report.unknown()

        int maxCritical =
            configuration.containsKey('maxCritical')
                ? configuration.maxCritical as int
                : 0

        int maxHigh =
            configuration.containsKey('maxHigh')
                ? configuration.maxHigh as int
                : Integer.MAX_VALUE

        int maxMedium =
            configuration.containsKey('maxMedium')
                ? configuration.maxMedium as int
                : Integer.MAX_VALUE

        int maxLow =
            configuration.containsKey('maxLow')
                ? configuration.maxLow as int
                : Integer.MAX_VALUE

        int maxUnknown =
            configuration.containsKey('maxUnknown')
                ? configuration.maxUnknown as int
                : Integer.MAX_VALUE

        if (critical > maxCritical) {
            throw new IllegalStateException(
                "Security policy failed: " +
                "Critical vulnerabilities = ${critical}, " +
                "allowed = ${maxCritical}"
            )
        }

        if (high > maxHigh) {
            throw new IllegalStateException(
                "Security policy failed: " +
                "High vulnerabilities = ${high}, " +
                "allowed = ${maxHigh}"
            )
        }

        if (medium > maxMedium) {
            throw new IllegalStateException(
                "Security policy failed: " +
                "Medium vulnerabilities = ${medium}, " +
                "allowed = ${maxMedium}"
            )
        }

        if (low > maxLow) {
            throw new IllegalStateException(
                "Security policy failed: " +
                "Low vulnerabilities = ${low}, " +
                "allowed = ${maxLow}"
            )
        }

        if (unknown > maxUnknown) {
            throw new IllegalStateException(
                "Security policy failed: " +
                "Unknown vulnerabilities = ${unknown}, " +
                "allowed = ${maxUnknown}"
            )
        }
    }
}
