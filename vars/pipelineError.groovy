def call(Exception exception) {
    error(exception.message ?: "Pipeline step failed")
}
