require "socket"
require "json"
require "file_utils"
require "log"
require "./system_state"
require "./system_manager"
require "./server_constants"

class Reactor
    def initialize(@port : Int32)
        @server = TCPServer.new("0.0.0.0", @port)
        @clients = [] of TCPSocket
    end
  
    def start
        Log.info { "Starting Reactor on port #{@port}" }
        spawn do
            loop do
                if client = @server.accept?
                    @clients << client
                    handle_client(client)
                end
            end
        rescue ex
            Log.error { "Server error: #{ex.message}" }
        end
    end

    private def handle_client(client)
        spawn do
            begin
                while message = client.gets
                    Log.info { "Received from client: #{message}" }
                    # Process the message and send a response
                    response = "Server received: #{message}\n"
                    client.puts response
                    client.flush
                    Log.info { "Sent to client: #{response}" }
                end
            rescue ex
                Log.error { "Client connection error: #{ex.message}" }
            ensure
                @clients.delete(client)
                client.close
                Log.info { "Client disconnected" }
            end
        end
    end
end