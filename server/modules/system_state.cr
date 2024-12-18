# This represents the state of the system.
enum SystemState
    # The system is currently active.
    Active
    # The system is currently being updated.
    Updating
    # The system is in development, thus only developers can connect.
    Private
    # The system has been terminated.
    Terminated
end