package li.netcube.mcvm.common.ui;

import li.netcube.mcvm.MCVM;
import li.netcube.mcvm.common.blocks.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.List;

public class ComputerContainerGUI extends GuiContainer {
    public static final int WIDTH = 176;
    public static final int HEIGHT = 156;

    private ComputerContainerTileEntity ccte;

    private static final ResourceLocation background = new ResourceLocation(MCVM.MODID, "textures/gui/computer.png");

    private InventoryPlayer inventoryPlayer;

    private TexturedButton powerOnButton;
    private TexturedButton powerOffButton;
    private TexturedButton resetButton;

    public ComputerContainerGUI(ComputerContainerTileEntity tileEntity, ComputerContainer container, InventoryPlayer inventoryPlayer) {
        super(container);

        this.inventoryPlayer = inventoryPlayer;
        this.ccte = tileEntity;
        xSize = WIDTH;
        ySize = HEIGHT;


    }

    @Override
    public void initGui()
    {
        super.initGui();

        powerOnButton = new TexturedButton( 1, guiLeft + 68, guiTop + 43, 20, 20, background, 176, 0, 176, 20, 176, 40, "Turn ON");
        powerOffButton = new TexturedButton( 2, guiLeft + 68, guiTop + 43, 20, 20, background, 196, 0, 196, 20, 196, 40, "Turn OFF");
        resetButton = new TexturedButton( 3, guiLeft + 88, guiTop + 43, 20, 20, background, 216, 0, 216, 20, 216, 40,"Reset");

        this.buttonList.add(powerOnButton);
        this.buttonList.add(powerOffButton);
        this.buttonList.add(resetButton);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(Blocks.computer.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(inventoryPlayer.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                ccte.powerOn();
                break;
            case 2:
                ccte.powerOff();
                break;
            case 3:
                ccte.resetSystem();
                break;
        }
        //super.actionPerformed(button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void updateScreen() {
        if (ccte.poweredOn) {
            powerOnButton.enabled = false;
            powerOnButton.visible = false;
            powerOffButton.enabled = true;
            powerOffButton.visible = true;
            resetButton.enabled = true;
        } else {
            powerOnButton.enabled = true;
            powerOnButton.visible = true;
            powerOffButton.enabled = false;
            powerOffButton.visible = false;
            resetButton.enabled = false;
        }
        super.updateScreen();
    }


}
