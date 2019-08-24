Always a Target that defaults to the Impact. It can be set with offset.

Three types of references:

IMPACT 			// Hit location. In the case of a secondary effect is equivilant to Initiator
INITIATOR		// The location the effect started at. Will be OWNER on primary effects.
OWNER			// The person who initiated the skill.


References can have sticky set

TRUE			// If the reference type is associated with an enitty, follow that entity
FALSE			// Stick with the original location


Some components depend on these locations implicitly, and some explicitly.

Particle styles dependent on radius for example always reference target