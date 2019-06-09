package com.derongan.minecraft.looty.config;

public class TestItems {
    public static final String BLAZE_REAP_YAML = "name: Blaze Reap\n" +
            "lore:\n" +
            "- Hello world\n" +
            "rarity: SPECIAL_GRADE\n" +
            "material: DIAMOND_PICKAXE\n" +
            "durability: 1\n" +
            "skills:\n" +
            "- trigger:\n" +
            "    modifiers: []\n" +
            "    hand: LEFT\n" +
            "  skill:\n" +
            "    actions:\n" +
            "    - Particle:\n" +
            "        fillStyle: TARGET\n" +
            "        particleName: EXPLOSION_HUGE\n" +
            "      Radius:\n" +
            "        radius: 3.0\n" +
            "      TargetChooser:\n" +
            "        offset:\n" +
            "          locationReferenceType: IMPACT\n" +
            "      Damage:\n" +
            "        damage: 5.0";
}
