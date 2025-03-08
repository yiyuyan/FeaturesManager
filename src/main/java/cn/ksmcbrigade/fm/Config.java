package cn.ksmcbrigade.fm;

import cn.ksmcbrigade.ca.config.ConfigBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;
import java.util.function.BiConsumer;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> LIST_BACKGROUND_COLOR = BUILDER.define("list_background_color","#D100A3FF");

    public static final ModConfigSpec.ConfigValue<String> LIST_BUTTON_HOVER_COLOR = BUILDER.define("list_button_hover_color","#8000C7FF");
    //public static final ModConfigSpec.ConfigValue<String> FEATURE_BACKGROUND_COLOR = BUILDER.define("feature_background_color","#B300A3FF");
    public static final ModConfigSpec.ConfigValue<String> FEATURE_ENABLED_COLOR = BUILDER.define("feature_background_enabled_color","#D100B7FF");
    public static final ModConfigSpec.ConfigValue<String> FEATURE_DISABLED_COLOR = BUILDER.define("feature_background_disabled_color","#D100A3FF");
    //public static final ModConfigSpec.ConfigValue<String> FEATURE_HOVER_COLOR = BUILDER.define("feature_background_hover_color","#CC00C7FF");

    static final ModConfigSpec SPEC = BUILDER.build();

    public static class ConfigBuilderEx extends ConfigBuilder{

        public ConfigBuilderEx(File file) {
            super(file);
        }

        public ConfigBuilderEx(String file, boolean forceConfigFolder) {
            super(file, forceConfigFolder);
        }

        public ConfigBuilderEx(String featureName){
            super("config/fm/features/"+featureName+".json",false);
        }

        public ConfigBuilderEx put(String key,Object o){
            this.data.put(key,o);
            return this;
        }

        public ConfigBuilderEx setCallbackT(BiConsumer<String, Object> callback) {
            super.setCallback(callback);
            return this;
        }
    }
}
