package com.derongan.minecraft.looty.skill;

public enum LocationReferenceType {
    /**
     * The initiator of the action. Usually a player.
     */
    INITIATOR,

    /**
     * Where the action was targeted. This could be a struck block, or a button that was activated.
     */
    IMPACT
}
