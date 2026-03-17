package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.miniboss.TheEndspeaker;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public abstract class EndspeakerAspect extends Buff {

    {
        type = buffType.POSITIVE;
    }

    protected int color;
    protected EndspeakerAbility ability;

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(color);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.ring( color );
        else target.sprite.clearRing();
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    public boolean giveAbility() {
        if (this.ability != null) {
            return TheEndspeaker.Status.activate(this.ability);
        }
        return false;
    }

    public enum EndspeakerAbility {
        SPELL_ABSORPTION,
        INCREASED_RANGE,
        RAMP_UP,
        CHARGE,
        HARDENING,
        CROWD_CONTROL_IMMUNE,

    }

    // Depth 36 pair (teal/aquamarine — magic)
    public static class SpellAbsorption extends EndspeakerAspect {
        {
            color = 0x00CCBB; // teal
            ability = EndspeakerAbility.SPELL_ABSORPTION;
        }
    }

    public static class IncreasedRange extends EndspeakerAspect {
        {
            color = 0x99FFEE; // aquamarine
            ability = EndspeakerAbility.INCREASED_RANGE;
        }
    }

    // Depth 37 pair (magenta/pink — aggression)
    public static class RampUp extends EndspeakerAspect {
        {
            color = 0xFF1188; // hot magenta
            ability = EndspeakerAbility.RAMP_UP;
        }
    }

    public static class Charge extends EndspeakerAspect {
        {
            color = 0xFF7799; // salmon pink
            ability = EndspeakerAbility.CHARGE;
        }
    }

    // Depth 38 pair (brown/amethyst — defense)
    public static class Hardening extends EndspeakerAspect {
        {
            color = 0x885533; // dark sienna
            ability = EndspeakerAbility.HARDENING;
        }
    }

    public static class CrowdControlImmune extends EndspeakerAspect {
        {
            color = 0xBB88FF; // soft amethyst
            ability = EndspeakerAbility.CROWD_CONTROL_IMMUNE;
        }
    }

    public static class Empowering extends EndspeakerAspect {
        {
            color = 0xFFCCEE; // pale rose — buff indicator color only
        }

        @Override
        public void fx(boolean on) {
            // do not interfere with the existing aura
        }
    }
}