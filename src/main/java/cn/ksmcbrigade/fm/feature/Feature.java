package cn.ksmcbrigade.fm.feature;

import cn.ksmcbrigade.ca.config.Config;
import cn.ksmcbrigade.fm.FeaturesManager;
import cn.ksmcbrigade.fm.enums.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class Feature {
    public final String name;
    public boolean enabled = false;
    public int key = -1;
    public boolean hide = false;
    private Config config;

    public Category type;

    public static final BiConsumer<String,Object> EMPTY_CONFIG_CALLBACK = (s,o)->{};

    public Feature(String name, boolean enabled, int key, @Nullable Config config, Category category){
        this.name = name;
        this.enabled = enabled;
        this.key = key;
        this.config = config;
        this.type = category;

        if(this.config!=null){
            final BiConsumer<String, Object> modifiedCallback = getStringObjectBiConsumer();
            try {
                Field field = this.config.getClass().getDeclaredField("callback");
                field.setAccessible(true);
                field.set(this.config,modifiedCallback);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private @NotNull BiConsumer<String, Object> getStringObjectBiConsumer() {
        final BiConsumer<String,Object> defaultCallback = this.config.callback;
        return (ss, oo)->{
            if(defaultCallback!=null && defaultCallback!=EMPTY_CONFIG_CALLBACK){
                try {
                    defaultCallback.accept(ss,oo);
                } catch (NullPointerException e) {
                }
            }
            try {
                this.onConfigUpdate(ss,oo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Feature(String name, boolean enabled,Category category){
        this(name,enabled,-1,null,category);
    }

    public Feature(String name,Category category,Config config,int key){
        this(name,false,key,config,category);
    }

    public Feature(String name,Category category,Config config){
        this(name,false,-1,config,category);
    }

    public Feature(String name,Category category){
        this(name,false,-1,null,category);
    }

    public void toggle() throws Exception{
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled) throws Exception {
        this.enabled = enabled;
        Minecraft MC = Minecraft.getInstance();
        if(enabled){
            enabled(MC);
        }
        else{
            disabled(MC);
        }
        FeaturesManager.saveFeaturesList();
    }

    public String getName(){return I18n.get(this.name);}

    public int getKey() {return key;}

    public void setKey(int key) {this.key = key;}

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public int length(){
        return getName().length();
    }

    public void enabled(Minecraft MC) throws Exception{}
    public void disabled(Minecraft MC) throws Exception{}

    public void tick(Minecraft MC, LocalPlayer player) throws Exception{}
    public void onConfigUpdate(String key,Object value) throws Exception{}

    public Component getDescribe(){return Component.empty();}
}
