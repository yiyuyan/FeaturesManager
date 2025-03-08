package cn.ksmcbrigade.fm.features;

import cn.ksmcbrigade.el.events.block.ApplyBlockInternetRangeEvent;
import cn.ksmcbrigade.el.events.entity.ApplyEntityInternetRangeEvent;
import cn.ksmcbrigade.fm.Config;
import cn.ksmcbrigade.fm.enums.Category;
import cn.ksmcbrigade.fm.feature.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;

public class Reach extends Feature {

    public int v;

    public Reach() throws IOException, NoSuchFieldException, IllegalAccessException {
        super(Reach.class.getSimpleName(),Category.COMBAT,new Config.ConfigBuilderEx("config/fm/features/Reach.json",false).put("range",32767).build());
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        NeoForge.EVENT_BUS.register(this);
        v = getConfig().getInt("range");
    }

    @Override
    public void disabled(Minecraft MC) throws Exception {
        NeoForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onConfigUpdate(String key, Object value) throws Exception {
        this.v = (int)value;
    }

    @SubscribeEvent
    public void block(ApplyBlockInternetRangeEvent event){
        event.value = v;
    }

    @SubscribeEvent
    public void entity(ApplyEntityInternetRangeEvent event){
        event.value = v;
    }

    @Override
    public Component getDescribe() {
        return Component.literal("It can put you in contact with entities or blocks that are farther away from you.");
    }
}
