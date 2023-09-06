package thunder.hack.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import thunder.hack.Thunderhack;
import thunder.hack.events.impl.EventSync;
import thunder.hack.events.impl.PacketEvent;
import thunder.hack.modules.Module;
import thunder.hack.setting.Setting;
import thunder.hack.utility.player.InteractionUtility;
import thunder.hack.utility.player.PlayerUtility;

public class NoSlow extends Module {
    public static Setting<mode> Mode = new Setting<>("Mode", mode.NCP);

    public NoSlow() {
        super("NoSlow", "NoSlow", Category.MOVEMENT);
    }

    @EventHandler
    public void onTick(EventSync event) {
        if (mc.player.isUsingItem()) {
            if (Mode.getValue() == mode.StrictNCP || Mode.getValue() == mode.NCP) {
                if (!mc.player.isRiding() && !mc.player.isSneaking()) {
                    if (Mode.getValue() == mode.StrictNCP)
                        sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                }
            }

            if (Mode.getValue() == mode.MusteryGrief && mc.player.getActiveHand() == Hand.OFF_HAND) {
                if (!mc.player.isRiding() && !mc.player.isSneaking()) {
                    sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                    if (mc.player.isOnGround() && !mc.options.jumpKey.isPressed()) {
                        mc.player.setVelocity(mc.player.getVelocity().x * 0.3, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.3);
                    } else if (mc.player.fallDistance > 0.2f)
                        mc.player.setVelocity(mc.player.getVelocity().x * 0.95f, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.95f);
                }
            }

            if (Mode.getValue() == mode.GrimOffHand && mc.player.getActiveHand() == Hand.OFF_HAND) {
                sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot % 8 + 1));
                sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
            }

            if (Mode.getValue() == mode.Matrix) {
                if (!Thunderhack.moduleManager.get(Strafe.class).isEnabled()) {
                    if (mc.player.isOnGround() && !mc.options.jumpKey.isPressed()) {
                        mc.player.setVelocity(mc.player.getVelocity().x * 0.3, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.3);
                    } else if (mc.player.fallDistance > 0.2f)
                        mc.player.setVelocity(mc.player.getVelocity().x * 0.95f, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.95f);
                } else {
                    if (!mc.player.isOnGround() && mc.player.fallDistance > 0.2f)
                        mc.player.setVelocity(mc.player.getVelocity().x * 0.7, mc.player.getVelocity().y, mc.player.getVelocity().z * 0.7f);
                }
            }
        }
    }

    public enum mode {
        NCP, StrictNCP, Matrix, GrimOffHand, MusteryGrief
    }

}
