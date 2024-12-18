require "socket"
require "yaml"
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
    
    def self.initialize(config : Hash(YAML::Any, YAML::Any))
        Log.info { "Initializing game world with config" }
        yaml_config = YAML::Any.new(config)
        @@settings = GameSettings.from_yaml(yaml_config)
        Log.info { "Game World initialized with settings: #{@@settings}" }
    rescue e : Exception
        Log.error { "Failed to initialize game world: #{e.message}" }
        raise e
    end
end