# An abstract class for writing content that allows the class to execute code when the server is started.
abstract class StartupListener
    # NOTE: This should NOT reference nonstatic class-local variables.
    abstract def startup
end