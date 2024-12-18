require "socket"
require "json"
require "file_utils"
require "log"
require "../../../../modules/system_state"
require "../../../../modules/system_manager"
require "../../../../modules/server_constants"
require "../../../../modules/reactor"
require "./game_settings"
require "../../../../modules/api/**"

class GameWorld
    @@settings : GameSettings?
    
    def self.settings
        @@settings
    end
    
    def self.initialize(config : Hash(String, JSON::Any))
        @@settings = GameSettings.new(config)
        # Use settings to initialize game world components
        Log.info { "Game World initialized with settings: #{@@settings}" }
    end
    # Add more game world related methods here
end