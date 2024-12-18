require "yaml"
require "log"

module Config
    class Parser
        property config : YAML::Any

        def initialize
            @config = YAML.parse("{}")
        end

        def self.load(file_path : String) : YAML::Any
            parser = new
            parser.parse(file_path)
            parser.config
        end

        def parse(file_path : String) : YAML::Any
            Log.info { "Loading config file: #{file_path}" }

            content = File.read(file_path)
            @config = YAML.parse(content)

            Log.info { "Configuration loaded successfully." }
            @config
        rescue e : YAML::ParseException
            Log.error { "Failed to parse YAML config file: #{e.message}" }
            raise e
        rescue e : File::NotFoundError
            Log.error { "Config file not found: #{file_path}" }
            raise e
        end

        def self.convert_keys_to_string(hash : Hash(YAML::Any, YAML::Any)) : Hash(String, YAML::Any)
            result = Hash(String, YAML::Any).new
            hash.each do |k, v|
                result[k.to_s] = v
            end
            result
        end
    end
end