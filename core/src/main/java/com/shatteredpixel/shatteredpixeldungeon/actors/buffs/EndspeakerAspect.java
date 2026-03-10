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
        if (on) target.sprite.aura( color );
        else target.sprite.clearAura();
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
            TheEndspeaker.Status.activate(this.ability);
            return true;
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

    public static class SpellAbsorption extends EndspeakerAspect {
        {
            color = 0x90D5FF00;
            ability = EndspeakerAbility.SPELL_ABSORPTION;
        }
    }

    public static class IncreasedRange extends EndspeakerAspect {
        {
            color = 0x90D5FF00;
            ability = EndspeakerAbility.INCREASED_RANGE;
        }
    }

    public static class RampUp extends EndspeakerAspect {
        {
            color = 0xFFFF9900;
            ability = EndspeakerAbility.RAMP_UP;
        }
    }

    public static class Charge extends EndspeakerAspect {
        {
            color = 0xFFFF9900;
            ability = EndspeakerAbility.CHARGE;
        }
    }

    public static class Hardening extends EndspeakerAspect {
        {
            color = 0xFF751800;
            ability = EndspeakerAbility.HARDENING;
        }
    }

    public static class CrowdControlImmune extends EndspeakerAspect {
        {
            color = 0xFF751800;
            ability = EndspeakerAbility.CROWD_CONTROL_IMMUNE;
        }
    }

    public static class Empowering extends EndspeakerAspect {
        {
            color = 0xFF000000;
        }
    }
}