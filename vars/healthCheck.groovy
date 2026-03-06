def call(Map config = [:]) {
    def url = config.url
    def maxRetries = config.maxRetries ?: 10
    def retryInterval = config.retryInterval ?: 10

    echo "Starting health check for ${url}..."
    
    for (int i = 1; i <= maxRetries; i++) {
        try {
            // -f: fail silently on server errors
            // -s: silent mode
            // -o /dev/null: ignore output
            sh "curl -fs -o /dev/null ${url}"
            echo "Health check passed! App is responding."
            return
        } catch (Exception e) {
            echo "Health check attempt ${i}/${maxRetries} failed. Retrying in ${retryInterval}s..."
            sleep(retryInterval)
        }
    }

    error "HEALTH CHECK FAILED: Application at ${url} is not responding after ${maxRetries * retryInterval} seconds."
}
