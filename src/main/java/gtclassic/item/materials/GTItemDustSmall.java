package gtclassic.item.materials;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import gtclassic.GTMod;
import gtclassic.GTRecipes;
import gtclassic.util.GTValues;
import gtclassic.util.color.GTColorItemInterface;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GTItemDustSmall extends Item implements IStaticTexturedItem, GTColorItemInterface {

	private String material;

	public GTItemDustSmall(String material) {
		this.material = material;
		setRegistryName(this.material + "_dustSmall");
		setUnlocalizedName(GTMod.MODID + "." + this.material + "_dustSmall");
		setCreativeTab(GTMod.creativeTabGT);
		setRecipes();
	}

	public void setRecipes() {
		String input = "dust" + this.material;
		GTRecipes.recipes.addShapelessRecipe(new ItemStack(this, 4), new Object[] { input });
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	public TextureAtlasSprite getTexture(int i) {
		return Ic2Icons.getTextures(GTMod.MODID + "_materials")[1];
	}

	@Override
	public Color getColor(ItemStack stack, int index) {
		return GTValues.getColor(this.material);
	}
}