package cn.ksmcbrigade.fm.managers;

import cn.ksmcbrigade.ca.config.Config;
import cn.ksmcbrigade.fm.feature.Feature;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigsManager {

    public static final File configDir = new File("config/fm");
    public static final File featureConfigDir = new File("config/fm/features");

    private final Map<Feature, Config> configs = new HashMap<>();

    public Map<Feature,Config> getAll(){
        return configs;
    }

    public void add(Feature featureKey, Config config){
        configs.put(featureKey, config);
    }

    @Nullable
    public Config get(Feature feature){
        return configs.get(feature);
    }

    @Nullable
    public Config get(Class<? extends Feature> f){
        for (Feature feature : configs.keySet()) {
            if(feature.getClass().equals(f)) return configs.get(feature);
        }
        return null;
    }

    @Nullable
    public Feature getFeature(Config config){
        for (Feature feature : configs.keySet()) {
            if(configs.get(feature).equals(config)){
                return feature;
            }
        }
        return null;
    }
}
