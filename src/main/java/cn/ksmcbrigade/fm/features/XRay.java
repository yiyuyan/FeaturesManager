package cn.ksmcbrigade.fm.features;

import cn.ksmcbrigade.el.events.block.BlockLightingEvent;
import cn.ksmcbrigade.el.events.misc.GetOptionValueEvent;
import cn.ksmcbrigade.el.events.render.RenderBlockEvent;
import cn.ksmcbrigade.fm.Config;
import cn.ksmcbrigade.fm.enums.Category;
import cn.ksmcbrigade.fm.feature.Feature;
import com.google.gson.JsonArray;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XRay extends Feature {

    public List<String> BLOCKS = new ArrayList<>();
    public boolean fullBright = false;

    public XRay() throws IOException, NoSuchFieldException, IllegalAccessException {
        super(XRay.class.getSimpleName(),Category.COMBAT,getC(),InputConstants.KEY_X);
    }

    public static cn.ksmcbrigade.ca.config.Config getC() throws IOException, NoSuchFieldException, IllegalAccessException {
        Config.ConfigBuilderEx builderEx = new Config.ConfigBuilderEx(XRay.class.getSimpleName());
        JsonArray array = new JsonArray();
        for (ResourceLocation location : BuiltInRegistries.BLOCK.keySet()) {
            if(location.toString().contains("ore")){
                array.add(location.toString());
            }
        }
        return builderEx.put("blocks",array).put("fullBright",true).build();
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        NeoForge.EVENT_BUS.register(this);
        BLOCKS = getConfig().getArray("blocks");
        fullBright = getConfig().getBoolean("fullBright");
        if(MC.levelRenderer!=null) MC.levelRenderer.allChanged();
    }

    @Override
    public void disabled(Minecraft MC) throws Exception {
        NeoForge.EVENT_BUS.unregister(this);
        if(MC.levelRenderer!=null) MC.levelRenderer.allChanged();
    }

    @Override
    public void onConfigUpdate(String key, Object value) {
        BLOCKS = getConfig().getArray("blocks");
        fullBright = getConfig().getBoolean("fullBright");
        Minecraft.getInstance().levelRenderer.allChanged();
    }

    @SubscribeEvent
    public void blockLighting(BlockLightingEvent event){
        event.value = 15;
    }

    @SubscribeEvent
    public void render(RenderBlockEvent event){
        event.render = BLOCKS.contains(BuiltInRegistries.BLOCK.getKey(event.block.getBlock()).toString());
    }

    @SubscribeEvent
    public void fullBright(GetOptionValueEvent event){
        if(fullBright && event.cap.equals(Minecraft.getInstance().options.gamma().toString())){
            event.value = 300d;
        }
    }

    @Override
    public Component getDescribe() {
        return Component.literal("It can make you look some blocks you can't see before.");
    }
}
