abstract class PersistWorld
    # Save the world state.
    abstract def save

    # Parse or load the world state.
    abstract def parse
end