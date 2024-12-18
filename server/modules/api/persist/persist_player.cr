require "json"

# An interface for writing content that allows data to be saved and loaded from player saves.
# Parsing is called *after* any [LoginListener] is executed.
# Saving is called *after* any [LogoutListener] is executed.
abstract class PersistPlayer
    # NOTE: This should NOT reference nonstatic class-local variables.
    # You need to fetch a player's specific instance of the data and save from that.
    abstract def savePlayer

    # NOTE: This should NOT reference nonstatic class-local variables.
    # You need to fetch a player's specific instance of the data and parse to that.
    abstract def parsePlayer
end