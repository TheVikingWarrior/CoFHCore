package cofh.gui.element;

import cofh.CoFHCore;
import cofh.gui.GuiBase;
import cofh.gui.GuiProps;
import cofh.gui.container.IAugmentableContainer;
import cofh.render.RenderHelper;
import cofh.util.MathHelper;
import cofh.util.StringHelper;

import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class TabAugment extends TabBase {

	public static boolean enable;
	public static int defaultSide = 1;
	public static int defaultHeaderColor = 0xe1c92f;
	public static int defaultSubHeaderColor = 0xaaafb8;
	public static int defaultTextColor = 0x000000;
	public static int defaultBackgroundColor = 0x089e4c;

	public static void initialize() {

		String category = "tab.augment";
		// enable = CoFHCore.configClient.get(category, "Enable", true);
		defaultSide = MathHelper.clampI(CoFHCore.configClient.get(category, "Side", defaultSide), 0, 1);
		defaultHeaderColor = MathHelper.clampI(CoFHCore.configClient.get(category, "ColorHeader", defaultHeaderColor), 0, 0xffffff);
		defaultSubHeaderColor = MathHelper.clampI(CoFHCore.configClient.get(category, "ColorSubHeader", defaultSubHeaderColor), 0, 0xffffff);
		defaultTextColor = MathHelper.clampI(CoFHCore.configClient.get(category, "ColorText", defaultTextColor), 0, 0xffffff);
		defaultBackgroundColor = MathHelper.clampI(CoFHCore.configClient.get(category, "ColorBackground", defaultBackgroundColor), 0, 0xffffff);
		CoFHCore.configClient.save();
	}

	public static ResourceLocation GRID_TEXTURE = new ResourceLocation(GuiProps.PATH_ELEMENTS + "Slot_Grid_Augment.png");

	IAugmentableContainer myContainer;

	int numAugments = 0;
	int slotsBorderX1 = 18;
	int slotsBorderX2 = slotsBorderX1 + 60;
	int slotsBorderY1 = 20;
	int slotsBorderY2 = slotsBorderY1 + 42;

	public TabAugment(GuiBase gui, IAugmentableContainer container) {

		this(gui, defaultSide, container);
	}

	public TabAugment(GuiBase gui, int side, IAugmentableContainer container) {

		super(gui, side);

		headerColor = defaultHeaderColor;
		subheaderColor = defaultSubHeaderColor;
		textColor = defaultTextColor;
		backgroundColor = defaultBackgroundColor;

		maxHeight = 92;
		maxWidth = 100;
		myContainer = container;

		numAugments = myContainer.getAugmentSlots().length;

		for (int i = 0; i < numAugments; i++) {
			myContainer.getAugmentSlots()[i].xDisplayPosition = -gui.getGuiLeft() - 16;
			myContainer.getAugmentSlots()[i].yDisplayPosition = -gui.getGuiTop() - 16;
		}
		myContainer.setAugmentLock(true);
		switch (numAugments) {
		case 4:
			slotsBorderX1 += 9;
		case 5:
		case 6:
			break;
		default:
			slotsBorderX1 += 9 * (3 - numAugments);
			slotsBorderX2 = slotsBorderX1 + 18 * numAugments + 6;
			slotsBorderY1 += 9;
			slotsBorderY2 -= 9;
		}
		myContainer.setAugmentLock(true);
	}

	@Override
	public void draw() {

		drawBackground();
		drawTabIcon("IconAugment");
		if (!isFullyOpened()) {
			return;
		}
		getFontRenderer().drawStringWithShadow(StringHelper.localize("info.cofh.augmentation"), posXOffset() + 18, posY + 6, headerColor);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void addTooltip(List<String> list) {

		if (!isFullyOpened()) {
			list.add(StringHelper.localize("info.cofh.augmentation"));
		}
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {

		if (!isFullyOpened()) {
			return false;
		}
		if (side == LEFT) {
			mouseX += currentWidth;
		}
		mouseX -= currentShiftX;
		mouseY -= currentShiftY;

		if (mouseX < slotsBorderX1 + offset() || mouseX >= slotsBorderX2 + offset() || mouseY < slotsBorderY1 || mouseY >= slotsBorderY2) {
			return false;
		}

		return true;
	}

	@Override
	protected void drawBackground() {

		super.drawBackground();

		if (!isFullyOpened()) {
			return;
		}
		float colorR = (backgroundColor >> 16 & 255) / 255.0F * 0.6F;
		float colorG = (backgroundColor >> 8 & 255) / 255.0F * 0.6F;
		float colorB = (backgroundColor & 255) / 255.0F * 0.6F;
		GL11.glColor4f(colorR, colorG, colorB, 1.0F);

		if (numAugments > 3) {
			gui.drawTexturedModalRect(posXOffset() + slotsBorderX1, posY + slotsBorderY1, 16, 20, (numAugments > 4 ? 18 * 3 : 18 * 2) + 6, 24 + 18);
		} else {
			gui.drawTexturedModalRect(posXOffset() + slotsBorderX1, posY + slotsBorderY1, 16, 20, 18 * numAugments + 6, 24);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.bindTexture(GRID_TEXTURE);

		switch (numAugments) {
		case 4:
			drawSlots(0, 0, 2);
			drawSlots(0, 1, 2);
			break;
		case 5:
			drawSlots(0, 0, 3);
			drawSlots(1, 1, 2);
			break;
		case 6:
			drawSlots(0, 0, 3);
			drawSlots(0, 1, 3);
			break;
		default:
			drawSlots(0, 0, numAugments);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setFullyOpen() {

		super.setFullyOpen();

		switch (numAugments) {
		case 4:
			for (int i = 0; i < numAugments; i++) {
				myContainer.getAugmentSlots()[i].xDisplayPosition = posXOffset() + slotsBorderX1 + 4 + 18 * (i % 2);
				myContainer.getAugmentSlots()[i].yDisplayPosition = posY + slotsBorderY1 + 4 + 18 * (i / 2);
			}
			break;
		case 5:
			for (int i = 0; i < numAugments; i++) {
				myContainer.getAugmentSlots()[i].xDisplayPosition = posXOffset() + slotsBorderX1 + 4 + 18 * (i % 3) + 9 * (i / 3);
				myContainer.getAugmentSlots()[i].yDisplayPosition = posY + slotsBorderY1 + 4 + 18 * (i / 3);
			}
			break;
		default:
			for (int i = 0; i < numAugments; i++) {
				myContainer.getAugmentSlots()[i].xDisplayPosition = posXOffset() + slotsBorderX1 + 4 + 18 * (i % 3);
				myContainer.getAugmentSlots()[i].yDisplayPosition = posY + slotsBorderY1 + 4 + 18 * (i / 3);
			}
		}
		myContainer.setAugmentLock(false);
	}

	@Override
	public void toggleOpen() {

		if (open) {
			for (int i = 0; i < numAugments; i++) {
				myContainer.getAugmentSlots()[i].xDisplayPosition = -gui.getGuiLeft() - 16;
				myContainer.getAugmentSlots()[i].yDisplayPosition = -gui.getGuiTop() - 16;
			}
			myContainer.setAugmentLock(true);
		}
		super.toggleOpen();
	}

	private void drawSlots(int xOffset, int yOffset, int slots) {

		gui.drawSizedTexturedModalRect(posXOffset() + slotsBorderX1 + 3 + 9 * xOffset, posY + slotsBorderY1 + 3 + 18 * yOffset, 0, 0, 18 * slots, 18, 72, 18);
	}

}
