package cn.ksmcbrigade.fm.features;

import cn.ksmcbrigade.fm.Config;
import cn.ksmcbrigade.fm.enums.Category;
import cn.ksmcbrigade.fm.feature.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public class Speed extends Feature {

    public double speed;
    public boolean enabledY;
    public boolean onlyOnJumping;

    public Speed() throws IOException, NoSuchFieldException, IllegalAccessException {
        super(Speed.class.getSimpleName(),Category.MOVEMENT,new Config.ConfigBuilderEx(Speed.class.getSimpleName()).put("speed",1.02d).put("onlyOnJumping",true).put("enabledY",true).build());
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        this.speed = getConfig().getDouble("speed");
        this.enabledY = getConfig().getBoolean("enabledY");
        this.onlyOnJumping = getConfig().getBoolean("onlyOnJumping");
    }

    @Override
    public void tick(Minecraft MC, LocalPlayer player) throws Exception {
        if(player==null) return;
        if(this.onlyOnJumping && !MC.options.keyJump.isDown()) return;
        player.setDeltaMovement(player.getDeltaMovement().multiply(speed,enabledY?speed:1.0d,speed));
    }

    @Override
    public void onConfigUpdate(String key, Object value) throws Exception {
        this.enabled(Minecraft.getInstance());
    }

    @Override
    public Component getDescribe() {
        return Component.literal("It can make you walk or fly more faster.");
    }
}
