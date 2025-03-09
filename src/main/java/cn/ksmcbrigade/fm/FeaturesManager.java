package cn.ksmcbrigade.fm;

import cn.ksmcbrigade.fm.api.RegisterFeaturesEvent;
import cn.ksmcbrigade.fm.feature.Feature;
import cn.ksmcbrigade.fm.features.Reach;
import cn.ksmcbrigade.fm.features.Speed;
import cn.ksmcbrigade.fm.features.XRay;
import cn.ksmcbrigade.fm.gui.FeatureHUD;
import cn.ksmcbrigade.fm.managers.CategoriesManager;
import cn.ksmcbrigade.fm.managers.ConfigsManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.BusBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(FeaturesManager.MODID)
public class FeaturesManager
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "fm";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CategoriesManager categoriesManager = new CategoriesManager();
    public static final ConfigsManager configsManager = new ConfigsManager();

    public static final File features = new File(ConfigsManager.configDir.getPath()+"/features.json");

    public static final IEventBus REGISTER_EVENT_BUS = BusBuilder.builder().classChecker(eventType -> {
        if (IModBusEvent.class.isAssignableFrom(eventType)) {
            throw new IllegalArgumentException("IModBusEvent events are not allowed on the common NeoForge bus! Use a mod bus instead.");
        }
    }).build();

    public static final ArrayList<Feature> noneRegisteredFeatures = new ArrayList<>();


    public FeaturesManager(IEventBus modEventBus, ModContainer modContainer) throws Exception {
        NeoForge.EVENT_BUS.register(this);
        REGISTER_EVENT_BUS.register(this);

        ConfigsManager.configDir.mkdir();
        ConfigsManager.featureConfigDir.mkdir();
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        //post register event to register other events
        REGISTER_EVENT_BUS.post(new RegisterFeaturesEvent(categoriesManager));
        for (Feature noneRegisteredFeature : noneRegisteredFeatures) {
            categoriesManager.add(noneRegisteredFeature.type,noneRegisteredFeature);
        }

        readFeaturesList();
        saveFeaturesList();
        LOGGER.info("FeaturesManager mod loaded.");
    }

    @SubscribeEvent
    public void keyInput(InputEvent.Key event) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        if(InputConstants.isKeyDown(MC.getWindow().getWindow(),InputConstants.KEY_RCONTROL)){
            MC.setScreen(new FeatureHUD());
        }
        for (Feature allFeature : categoriesManager.getAllFeatures()) {
            if(allFeature.getKey()==-1) continue;
            if(InputConstants.isKeyDown(MC.getWindow().getWindow(),allFeature.getKey())){
                allFeature.toggle();
            }
        }
    }

    @SubscribeEvent
    public void tick(PlayerTickEvent.Pre event){
        categoriesManager.getAllFeatures().stream().filter(f->f.enabled).forEach(f -> {
            try {
                f.tick(Minecraft.getInstance(),Minecraft.getInstance().player);
            } catch (Exception e) {
                LOGGER.error("Failed in the feature tick.",e);
            }
        });
    }

    @SubscribeEvent
    public void registerFeatures(RegisterFeaturesEvent event) throws IOException, NoSuchFieldException, IllegalAccessException {
        //event.register(new Reach(),new Speed(),new XRay());
    }

    public static void readFeaturesList() throws Exception {
        if(!features.exists()) return;
        JsonArray array = JsonParser.parseString(FileUtils.readFileToString(features)).getAsJsonArray();
        for (JsonElement jsonElement : array) {
            if(jsonElement instanceof JsonObject object){
                String name = object.get("name").getAsString();
                String category = object.get("category").getAsString();
                int key = -2;
                try {
                    key = object.get("key").getAsInt();
                } catch (Exception e) {
                    //nothing now
                }
                boolean enabled = object.get("enabled").getAsBoolean();

                Feature feature = categoriesManager.getFeature(name);
                if(feature!=null){
                    feature.setEnabled(enabled);
                    if(key!=-2) feature.setKey(key);
                    LOGGER.info("Set a feature at {} category to {}.",category,enabled);
                }
            }
        }
    }

    public static void saveFeaturesList() throws IOException{
        JsonArray array = new JsonArray();
        for (Feature allFeature : categoriesManager.getAllFeatures()) {
            JsonObject object = new JsonObject();
            object.addProperty("name",allFeature.name);
            object.addProperty("category",allFeature.type.name);
            object.addProperty("enabled",allFeature.enabled);
            object.addProperty("key",allFeature.key);
            array.add(object);
        }
        FileUtils.writeStringToFile(features, array.toString());
    }

    public static void readAndSave() throws Exception{
        readFeaturesList();
        saveFeaturesList();
    }
}
