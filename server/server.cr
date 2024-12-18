require "socket"
require "json"
require "yaml"
require "log"
require "path"
require "file_utils"
require "./modules/system_state"
require "./modules/system_manager"
require "./modules/server_constants"
require "./modules/reactor"
require "./src/main/core/world/game_settings"
require "./src/main/core/world/game_world"

class Server
    VERSION = "0.1.0"

    property start_time : Time
    property last_heartbeat : Time
    property running : Bool
    property reactor : Reactor?
    property network_reachability : NetworkReachability

    @config : Hash(String, JSON::Any)

    def initialize
        @start_time = Time.utc
        @last_heartbeat = Time.utc
        @running = false
        @reactor = nil
        @network_reachability = NetworkReachability::Reachable
        @config = Hash(String, JSON::Any).new
    end

    def self.main(args : Array(String))
        server = new
        server.run(args)
    end

    def run(args : Array(String))
        parse_config(args)
        initialize_game_world
        setup_networking
        setup_console_commands
        setup_watchdog if ServerConstants::WATCHDOG_ENABLED

        @running = true
        Log.info { "Server started in #{Time.utc - @start_time} milliseconds." }

    # Main server loop is here.
        while @running
        # Server logic is here.
            sleep 1
        end
    end

    private def parse_config(args)
        # Get the directory where server.cr is located, cause is being stupid.
        server_dir = Path[__FILE__].parent

        # Construct path to config file relative to server directory, cause is being stupid.
        default_config = server_dir.join("worlds", "default.conf")
        config_file = args.empty? ? default_config.to_s : args[0]

        Log.info { "Using config file: #{config_file}" }
        config_content = File.read(config_file)
        @config = JSON.parse(config_content).as_h
        Log.info { "Configuration loaded successfully." }
    rescue e : JSON::ParseException
        Log.error { "Failed to parse config file: #{e.message}" }
        raise e
    rescue e : File::NotFoundError
        Log.error { "Config file not found: #{config_file}" }
        raise e
    end

    private def initialize_game_world
        GameWorld.initialize(@config)
        Log.info { "Game World initialized successfully." }
    end

    private def setup_networking
        # + GameWorld.settings.not_nil!.world_id
        port = 43594
        @reactor = Reactor.new(port)
        @reactor.not_nil!.start
    rescue e : Socket::BindError
        Log.error { "Port #{port} is already in use!" }
        raise e
    end

    private def setup_console_commands
        spawn do
            while @running
                command = gets
                case command
                when "stop"
                    @running = false
                    SystemManager.flag(SystemState::Terminated)
                when "update"
                    SystemManager.flag(SystemState::Updating)
                when "help", "commands"
                    print_commands
                when "restartworker"
                    SystemManager.flag(SystemState::Active)
                end
            end
            Log.info { "Console command listener started." }
        end
    end

    private def setup_watchdog
        spawn do
            sleep 20.seconds
            while @running
                check_connectivity
                check_heartbeat
                SystemManager.update
                sleep 625.milliseconds
            end
        end
    end

    private def check_connectivity
        # Implement connectivity check logic.
    end

    private def check_heartbeat
        if Time.utc - @last_heartbeat > 2.hours && @running
            Log.error { "Triggering reboot due to heartbeat timeout" }
            create_thread_dump
            exit(0) unless SystemManager.terminated?
        end
    end

    private def create_thread_dump
        # Implement thread dump logic.
    end

    private def print_commands
        puts "stop - stop the server"
        puts "update - initiate an update"
        puts "help, commands - show this"
        puts "restartworker - Reboot the major update worker"
    end
end

enum NetworkReachability
    Reachable
    Unreachable
end

# Main execution.
begin
    Server.main(ARGV)
rescue ex
    Log.error { "An error occurred: #{ex.message}" }
    ex.backtrace.each { |line| Log.error { line } }
end