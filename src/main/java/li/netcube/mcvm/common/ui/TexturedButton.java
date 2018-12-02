package li.netcube.mcvm.common.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TexturedButton extends GuiButton {

    private ResourceLocation texture;
    private int defaultX, defaultY;
    private int hoverX, hoverY;
    private int disabledX, disabledY;

    private List<String> toolTip = new ArrayList<String>();

    public int /*x,y,*/w,h;

    public TexturedButton(int buttonId, int x, int y, int w, int h, ResourceLocation texture, int defaultX, int defaultY, int hoverX, int hoverY, int disabledX, int disabledY, String toolTip) {
        super(buttonId, x, y, w, h, "");
        this.texture = texture;

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;


        this.defaultX = defaultX;
        this.defaultY = defaultY;

        this.hoverX = hoverX;
        this.hoverY = hoverY;

        this.disabledX = disabledX;
        this.disabledY = disabledY;

        this.toolTip.add(toolTip);
    }

    public void setX(int x) {this.x = x;}

    public void setY(int y) {this.y = y;}


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(texture);
        if (this.visible) {
            if (!this.enabled) {
                this.drawTexturedModalRect(this.x, this.y, this.disabledX, this.disabledY, this.w, this.h);
            } else if (this.hovered) {
                this.drawTexturedModalRect(this.x, this.y, this.hoverX, this.hoverY, this.w, this.h);
            } else {
                this.drawTexturedModalRect(this.x, this.y, this.defaultX, this.defaultY, this.w, this.h);
            }
        }

        this.mouseDragged(mc, mouseX, mouseY);

        //super.drawButton(mc, mouseX, mouseY, partialTicks);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
