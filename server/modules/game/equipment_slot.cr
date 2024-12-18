enum EquipmentSlot
    # Top row (row 1).
    HEAD
    FACE
    SPECIAL
    # Row 2.
    BACK
    NECK
    AMMUNITION
    # Row 3.
    RIGHTHAND
    BODY
    LEFTHAND
    # Row 4.
    WRISTS
    LEGS
    POCKET_1
    # Row 5.
    HANDS
    FEETS
    POCKET_2
    # Row 6.
    FINGERS
    TOES
    POCKET_3
    # Hidden options if needed.
    HIDDEN_1
    HIDDEN_2
    HIDDEN_3
end

class EquipmentSlotHelper
    @@slot_map = {} of String => EquipmentSlot
  
    def self.init_slot_map
        @@slot_map = {
            # Top row (row 1).
            "head" => EquipmentSlot::HEAD, "helm" => EquipmentSlot::HEAD, "helmet" => EquipmentSlot::HEAD,
            "face" => EquipmentSlot::FACE, "mask" => EquipmentSlot::FACE,
            "special" => EquipmentSlot::SPECIAL,
            # Row 2.
            "back" => EquipmentSlot::BACK, "cape" => EquipmentSlot::BACK, "backpack" => EquipmentSlot::BACK,
            "neck" => EquipmentSlot::NECK, "amulet" => EquipmentSlot::NECK, "necklace" => EquipmentSlot::NECK,
            "ammunition" => EquipmentSlot::AMMUNITION, "ammo" => EquipmentSlot::AMMUNITION,
            # Row 3.
            "righthand" => EquipmentSlot::RIGHTHAND, "main" => EquipmentSlot::RIGHTHAND,
            "body" => EquipmentSlot::BODY, "torso" => EquipmentSlot::BODY, "chest" => EquipmentSlot::BODY,
            "lefthand" => EquipmentSlot::LEFTHAND, "off" => EquipmentSlot::LEFTHAND,
            # Row 4.
            "wrists" => EquipmentSlot::WRISTS, "wrist" => EquipmentSlot::WRISTS, "brace" => EquipmentSlot::WRISTS, "bracelet" => EquipmentSlot::WRISTS, "watch" => EquipmentSlot::WRISTS,
            "legs" => EquipmentSlot::LEGS, "leg" => EquipmentSlot::LEGS,
            # Row 5.
            "hands" => EquipmentSlot::HANDS, "hand" => EquipmentSlot::HANDS,
            "feets" => EquipmentSlot::FEETS, "feet" => EquipmentSlot::FEETS,
            # Row 6.
            "fingers" => EquipmentSlot::FINGERS, "ring" => EquipmentSlot::FINGERS,
            "toes" => EquipmentSlot::TOES, "toering" => EquipmentSlot::TOES,
            # 
            "weapon" => [EquipmentSlot::RIGHTHAND, EquipmentSlot::LEFTHAND],
            "shield" => [EquipmentSlot::RIGHTHAND, EquipmentSlot::LEFTHAND],
            "pocket" => [EquipmentSlot::POCKET_1, EquipmentSlot::POCKET_2, EquipmentSlot::POCKET_3],
            "pockets" => [EquipmentSlot::POCKET_1, EquipmentSlot::POCKET_2, EquipmentSlot::POCKET_3],
        }
    end
  
    # Return the equipment slot by name. Return nil if matching no slot.
    def self.slot_by_name(input : String) : EquipmentSlot?
        @@slot_map[input.downcase]?
    end
end
  
EquipmentSlotHelper.init_slot_map