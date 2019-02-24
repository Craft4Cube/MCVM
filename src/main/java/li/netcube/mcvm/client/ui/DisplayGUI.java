package li.netcube.mcvm.client.ui;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.exceptions.AuthenticationFailedException;
import com.shinyhut.vernacular.client.rendering.ColorDepth;
import li.netcube.mcvm.MCVM;
//import li.netcube.mcvm.common.ui.TexturedButton;
import li.netcube.mcvm.common.ui.TexturedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import static li.netcube.mcvm.util.vnc.Keymap.keyConvert;

@SideOnly(Side.CLIENT)
public class DisplayGUI extends GuiScreen {
    private VernacularConfig config;
    private VernacularClient client;
    private Image lastFrame = null;
    private DynamicTexture frame = null;

    private BufferedImage emptyImage;

    private static final ResourceLocation theWall = new ResourceLocation(MCVM.MODID, "textures/gui/display.png");

    public Integer VNCPort;
    public String VNCPassword;

    private int retryCounter = 5;

    private TexturedButton closeButton;
    private TexturedButton ctrlAltDelButton;

    private Integer lastW=0, lastH=0, lastVW=0, lastVH=0;

    @Override
    public void initGui() {
        super.initGui();

        if (!Minecraft.getMinecraft().world.isRemote) {
            return;
        }

        emptyImage = new BufferedImage(720, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = emptyImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,720,400);

        closeButton = new TexturedButton(1, 0, 0, 11, 11, theWall, 118,11, 118, 22, 118,0, "Close");
        ctrlAltDelButton = new TexturedButton(2, 0, 0, 11, 11, theWall, 129,11, 129, 22, 129,0, "Send Ctrl+Alt+Del");

        this.buttonList.add(closeButton);
        this.buttonList.add(ctrlAltDelButton);

        config = new VernacularConfig();
        config.setColorDepth(ColorDepth.BPP_24);
        config.setErrorListener(e -> {
            MCVM.logger.error(e.getMessage());
            MCVM.logger.info(e.getClass());
        });
        config.setTargetFramesPerSecond(30);
        config.setFramebufferUpdateListener(this::renderFrame);
        config.setPasswordSupplier(new Supplier<String>() {
            @Override
            public String get() {
                return VNCPassword;
            }
        });
        client = new VernacularClient(config);
        connect();

        Keyboard.enableRepeatEvents(true);
    }

    public void connect() {
        if (client.isRunning()) {client.stop();}
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            client.start("localhost", VNCPort);
            //System.out.println("localhost:" + VNCPort);
        } else {
            client.start(Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":")[0], VNCPort);
            //System.out.println(Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":")[0] + ":" + VNCPort);
        }
    }

    @Override
    public void onGuiClosed()
    {
        try
        {
            client.stop();
        } catch (Exception ignored) {}
        Keyboard.enableRepeatEvents(false);
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                mc.player.closeScreen();
                break;
            case 2:
                sendCtrlAltDel();
                break;
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int w = this.width;
        int h = this.height;


        if (client == null || !client.isRunning()) {
            lastFrame = emptyImage;
        }

        if (lastFrame != null) {
            if (frame != null) {
                frame.deleteGlTexture();
            }

            frame = new DynamicTexture((BufferedImage) lastFrame);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, frame.getGlTextureId());
            //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            h = lastFrame.getHeight(null) / 2;
            w = lastFrame.getWidth(null) / 2;
            DisplayDrawTexturedModalRect((this.width - w)/2, (this.height - h)/2, w, h);
        }

        int imgX = (this.width - w)/2;
        int imgY = (this.height - h)/2;

        GL11.glEnable(GL11.GL_BLEND);
        mc.getTextureManager().bindTexture(theWall);

        for (int x = imgX+5; x < imgX + w - 5; x++) {
            drawTexturedModalRect(x, imgY-9, 56, 0, 1, 14);                                // Top
            drawTexturedModalRect(x, imgY+h-5, 70, 0, 1, 14);                              // Bottom
        }

        for (int y = imgY+5; y < imgY + h - 5; y++) {
            drawTexturedModalRect(imgX-9, y, 28, 0, 14, 1);                                // Left
            drawTexturedModalRect(imgX+w-5, y, 42, 0, 14, 1);                              // Right
        }

        drawTexturedModalRect(imgX-9, imgY-9, 0, 0, 14, 14);        // Top Left
        drawTexturedModalRect(imgX+w-5, imgY-9, 14, 0, 14, 14);     // Top Right
        drawTexturedModalRect(imgX-9, imgY+h-5, 0, 14, 14, 14);     // Bottom Left
        drawTexturedModalRect(imgX+w-5, imgY+h-5, 14, 14, 14, 14);  // Bottom Right
        drawTexturedModalRect(imgX+w-25, imgY-19, 84, 0, 34, 13);  // Button Bar


        checkMouse((this.width - w), h, w*2, h*2);

        if (lastW != w || lastH != h || lastVW != this.width || lastVH != this.height) {
            if (closeButton != null) {
                closeButton.setX(imgX + w - 7);
                closeButton.setY(imgY - 14);
            }

            if (ctrlAltDelButton != null) {
                ctrlAltDelButton.setX(imgX + w - 20);
                ctrlAltDelButton.setY(imgY - 14);
            }
            lastW = w;
            lastH = h;
            lastVW = this.width;
            lastVH = this.height;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void renderFrame(Image rawFrame) {
        lastFrame = rawFrame;
    }

    public void DisplayDrawTexturedModalRect(int x, int y, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(x, y+height, 0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(x + width, y+height, 0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(x + width, y, 0D).tex(1.0D, 0.0D).endVertex();
        vertexBuffer.pos(x, y, 0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
    }

    int lastMouseX = 0;
    int lastMouseY = 0;
    boolean lastMouseL = false;
    boolean lastMouseM = false;
    boolean lastMouseR = false;

    public void checkMouse(int bX, int bY, int bW, int bH) {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        float guiScale = scaledresolution.getScaleFactor()/2f;
        if (client != null && client.isRunning()) {
            int mouseX = Mouse.getX();
            int mouseY = Mouse.getY();
            bW = (int)(bW*guiScale);
            bH = (int)(bH*guiScale);
            bX = (int)(bX*guiScale);
            bY = (int)(bY*guiScale);
            mouseX = (int)(mouseX*guiScale);
            mouseY = (int)(mouseY*guiScale);
            if (lastMouseX != mouseX || lastMouseY != mouseY) {
                System.out.println(mouseX);
                System.out.println(mouseY);
                System.out.println(bX);
                System.out.println(bY);
                System.out.println(bW);
                System.out.println(bH);
                System.out.println("-------------");
                lastMouseY = mouseY; lastMouseX = mouseX;

                mouseY = this.height - mouseY;
                System.out.println(mouseX);
                System.out.println(mouseY);
                System.out.println("-------------");

                mouseX = mouseX - bX;
                mouseY = mouseY + bY;
                System.out.println(mouseX);
                System.out.println(mouseY);
                System.out.println("-------------");


                if (mouseX < 0) {mouseX = 0;}
                if (mouseX > bW) {mouseX = bW;}
                if (mouseY < 0) {mouseY = 0;}
                if (mouseY > bH) {mouseY = bH;}

                client.moveMouse((int)(mouseX/guiScale), (int)(mouseY/guiScale));
            }

            boolean mouseL = Mouse.isButtonDown(0);
            if (lastMouseL != mouseL) {
                client.updateMouseButton(0, mouseL);
                lastMouseL = mouseL;
            }

            boolean mouseR = Mouse.isButtonDown(1);
            if (lastMouseR != mouseR) {
                client.updateMouseButton(2, mouseR);
                lastMouseR = mouseR;
            }

            boolean mouseM = Mouse.isButtonDown(2);
            if (lastMouseM != mouseM) {
                client.updateMouseButton(1, mouseM);
                lastMouseM = mouseM;
            }
        }
    }

    @Override
    protected void keyTyped(char ch, int key) throws IOException
    {}


    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
    }


    @Override
    public void handleKeyboardInput() throws IOException
    {
        int key = Keyboard.getEventKey();
        int ch = Keyboard.getEventCharacter();

        if (client != null && client.isRunning()) {
            client.keyPress(keyConvert(key, ch), '\u0000', Keyboard.isKeyDown(Keyboard.getEventKey()));
        }
    }

    private void sendCtrlAltDel() {
        if (client != null && client.isRunning()) {
            client.keyPress(keyConvert(Keyboard.KEY_LCONTROL, '\u0000'), '\u0000', true);
            client.keyPress(keyConvert(Keyboard.KEY_LMENU, '\u0000'), '\u0000', true);
            client.keyPress(keyConvert(Keyboard.KEY_DELETE, '\u0000'), '\u0000', true);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.keyPress(keyConvert(Keyboard.KEY_LCONTROL, '\u0000'), '\u0000', false);
            client.keyPress(keyConvert(Keyboard.KEY_LMENU, '\u0000'), '\u0000', false);
            client.keyPress(keyConvert(Keyboard.KEY_DELETE, '\u0000'), '\u0000', false);
        }
    }

    private int scaleMouseX(int x, int frameW, int containerW) {
        if (lastFrame == null) {
            return x;
        }
        return (int) (x * ((double) frameW / containerW));
    }

    private int scaleMouseY(int y, int frameH, int containerH) {
        if (lastFrame == null) {
            return y;
        }
        return (int) (y * ((double) frameH / containerH));
    }
}