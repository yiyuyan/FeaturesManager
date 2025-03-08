package cn.ksmcbrigade.fm.gui;

import cn.ksmcbrigade.fm.Config;
import cn.ksmcbrigade.fm.FeaturesManager;
import cn.ksmcbrigade.fm.enums.Category;
import cn.ksmcbrigade.fm.feature.Feature;
import cn.ksmcbrigade.fm.gui.widgets.FeatureButton;
import cn.ksmcbrigade.rl.Color;
import cn.ksmcbrigade.rl.widgets.button.Button;
import cn.ksmcbrigade.rl.widgets.button.ButtonBuilder;
import cn.ksmcbrigade.rl.widgets.editbox.EditBox;
import cn.ksmcbrigade.rl.widgets.editbox.EditBoxBuilder;
import cn.ksmcbrigade.rl.widgets.list.OptionList;
import cn.ksmcbrigade.rl.widgets.list.entry.AbstractWidgetEntry;
import cn.ksmcbrigade.rl.widgets.list.entry.ListEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureHUD extends Screen {

    private final Map<Button,Boolean> showFlags = new HashMap<>();

    private final ArrayList<Feature> features = new ArrayList<>();

    public OptionList searchList;

    public FeatureHUD() {
        super(Component.nullToEmpty("FeatureHUD"));
    }

    @Override
    protected void init() {

        this.features.clear();

        try{
            Color backgroundColor = Color.parse(Config.LIST_BACKGROUND_COLOR.get());

            Color hoverColor = Color.parse(Config.LIST_BUTTON_HOVER_COLOR.get());

            Color enabledColor = Color.parse(Config.FEATURE_ENABLED_COLOR.get());
            Color disabledColor = Color.parse(Config.FEATURE_DISABLED_COLOR.get());

            List<Category> categories = FeaturesManager.categoriesManager.getAllCategories().stream().toList();
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                Button button = new ButtonBuilder(Component.nullToEmpty(category.name),backgroundColor,hoverColor).noOutlines().build();
                OptionList list = new OptionList(65, (int) (this.height*0.95),0,12);

                for (Feature feature : FeaturesManager.categoriesManager.getFeatures(category)) {
                    if(feature.hide) continue;
                    features.add(feature);
                    FeatureButton featureButton = new FeatureButton(0,0,65,12,Component.nullToEmpty(feature.name),feature,enabledColor,disabledColor);
                    if(!feature.getDescribe().toString().isEmpty()) featureButton.setTooltip(Tooltip.create(feature.getDescribe()));
                    list.addEntry(new AbstractWidgetEntry(featureButton));
                }

                button.setPosition(2+i*(65+2),2);

                this.setAndAdd(button,list);
            }

            int last = categories.size()*(65+2)+3;
            EditBox box = new EditBoxBuilder(backgroundColor).setWH(65,18).setPos(2+2+last,4).draggable().backgroundSize(2).hint(Component.nullToEmpty("Search")).onDragged((b,x,y)->{
                searchList.setPosition(b.getX(),b.getY()+b.getHeight());
            }).build();
            searchList = new OptionList(65,(int) (this.height*0.95),box.getY()+box.getHeight(),12);
            searchList.setPosition(box.getX(),box.getY()+box.getHeight());
            searchList.setWidth(65);
            searchList.setRowWidth(65);

            box.setResponder((s)->{
                searchList.clearEntries();
                searchList.setPosition(box.getX(),box.getY()+box.getHeight());
                if(!s.isEmpty()){
                    for (Feature feature : features) {
                        if(feature.getName().toLowerCase().contains(s.toLowerCase())){
                            FeatureButton featureButton = new FeatureButton(0,0,65,12,Component.nullToEmpty(feature.name),feature,enabledColor,disabledColor);
                            if(!feature.getDescribe().toString().isEmpty()) featureButton.setTooltip(Tooltip.create(feature.getDescribe()));
                            searchList.addEntry(new AbstractWidgetEntry(featureButton));
                        }
                    }
                }
            });
            searchList.setPosition(box.getX(),box.getY()+box.getHeight());

            this.addRenderableWidget(box);
            this.addRenderableWidget(searchList);
        } catch (Exception e) {
            e.printStackTrace();
            this.onClose();
        }
    }

    private void setAndAdd(Button button, OptionList list){
        button.setWidth(65);
        button.setHeight(18);
        button.draggable = true;

        button.onDragged = (b,x,y)->{
            list.setX(b.getX());
            list.setY(b.getY()+b.getHeight());
        };
        button.onPress = (b)->{
            boolean flag = showFlags.get(b);
            showFlags.put(b,!flag);
            list.render = !flag;
        };

        list.setX(button.getX());
        list.setY(button.getY()+button.getHeight());
        list.setHeight((int) (this.height*0.95));
        list.setWidth(65);
        list.setRowWidth(65);

        showFlags.put(button,true);

        this.addRenderableWidget(button);
        this.addRenderableWidget(list);
    }

    @Override
    public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
        searchList.renderWidget(p_281549_, p_281550_, p_282878_, p_282465_);
    }
}
