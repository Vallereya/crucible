require "socket"
require "json"
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

    def initialize(config : Hash(String, JSON::Any))
        server_config = config["server"].as_h
        @name = server_config["name"].as_s
        @world_id = server_config["world_id"].as_i
        @port = server_config["port"]?.try(&.as_i) || 43594
        @max_players = server_config["max_players"]?.try(&.as_i) || 2000
    end

    def to_s
        "World ID: #{@world_id}, Name: #{@name}, Port: #{@port}, Max Players: #{@max_players}"
    end
end