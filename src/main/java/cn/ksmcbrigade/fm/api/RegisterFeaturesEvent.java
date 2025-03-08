package cn.ksmcbrigade.fm.api;

import cn.ksmcbrigade.fm.feature.Feature;
import cn.ksmcbrigade.fm.managers.CategoriesManager;
import net.neoforged.bus.api.Event;

public class RegisterFeaturesEvent extends Event {
    public final CategoriesManager categoriesManager;

    public RegisterFeaturesEvent(CategoriesManager categoriesManager){
        this.categoriesManager = categoriesManager;
    }

    public void register(Feature... features){
        for (Feature feature : features) {
            categoriesManager.add(feature.type,feature);
        }
    }
}
