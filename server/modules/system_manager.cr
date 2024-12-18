require "./system_state"
require "./security/encryption_manager"

class SystemManager
    @@state = SystemState::Active

    def self.flag(new_state : SystemState)
        @@state = new_state
    end

    def self.update
        case @@state
        when .updating?
            # Perform update logic.
            Log.info { "System is updating..." }
        when .active?
            # Normal operation logic.
            Log.info { "System is active and running normally." }
        when .terminated?
            Log.info { "System is terminated." }
        end
    end

    def self.terminated?
        @@state == SystemState::Terminated
    end
end