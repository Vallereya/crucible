require "socket"
require "yaml"
require "file_utils"
require "log"
require "../../../../modules/system_state"
require "../../../../modules/system_manager"
require "../../../../modules/server_constants"
require "../../../../modules/reactor"

struct GameSettings
    property world_id : Int32
    property name : String
    property port : Int32
    property max_players : Int32

    def initialize(@world_id : Int32, @name : String, @port : Int32 = 43594, @max_players : Int32 = 2000)
    end

    def self.default_settings
        new(
            world_id: 1,
            name: "Default",
            port: 43594,
            max_players: 2000
        )
    end

    def self.from_yaml(config : YAML::Any) : GameSettings
        server_config = config["server"]
        
        name = server_config["name"].as_s
        # Handle world_id as a number directly
        world_id = server_config["world_id"].as_i
        port = server_config["port"]?.try(&.as_i) || 43594
        max_players = server_config["max_players"]?.try(&.as_i) || 2000

        new(
            world_id: world_id,
            name: name,
            port: port,
            max_players: max_players
        )
    rescue e : KeyError
        Log.error { "Missing required configuration key in server section: #{e.message}" }
        raise e
    rescue e : Exception
        Log.error { "Error parsing server configuration: #{e.message}" }
        raise e
    end

    def to_s
        "World ID: #{@world_id}, Name: #{@name}, Port: #{@port}, Max Players: #{@max_players}"
    end
end