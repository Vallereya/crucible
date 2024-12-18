# An abstract class for writing content that allows the class to execute code as the server is shutting down.
abstract class ShutdownListener
    # NOTE: This should NOT reference nonstatic class-local variables.
    abstract def shutdown
end