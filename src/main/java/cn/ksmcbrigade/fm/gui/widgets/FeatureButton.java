package cn.ksmcbrigade.fm.gui.widgets;

import cn.ksmcbrigade.ca.gui.ConfigGui;
import cn.ksmcbrigade.fm.feature.Feature;
import cn.ksmcbrigade.rl.Color;
import cn.ksmcbrigade.rl.widgets.button.Button;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class FeatureButton extends Button {

    public final Feature feature;
    public final Color enabeldColor;
    public final Color disabledColor;

    public FeatureButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Feature feature,Color enabledColor,Color disabeldColor) {
        super(pX, pY, pWidth, pHeight, pMessage, enabledColor,disabeldColor, (b)->{
            try {
                feature.toggle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.feature = feature;
        this.enabeldColor = enabledColor;
        this.disabledColor = disabeldColor;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.pressing = GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), 0) == 1;
        Minecraft minecraft = Minecraft.getInstance();
        if(GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(),GLFW.GLFW_MOUSE_BUTTON_2)==1 && this.feature.getConfig()!=null){
            if(this.isHovered()) Minecraft.getInstance().setScreen(new ConfigGui(Minecraft.getInstance().screen,this.feature.getConfig()));
        }
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        int color = (feature.enabled ? this.enabeldColor : this.disabledColor).toInt();
        if(this.isHoveredOrFocused()){
            Color or = Color.parse(color);
            float a = or.g()-0.0035f;
            if(a<0) a = 0.1f;
            color = new Color(or.r(),or.g(),or.g(),a).toInt();
        }
        pGuiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? this.activeColor : this.notActiveColor;
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
