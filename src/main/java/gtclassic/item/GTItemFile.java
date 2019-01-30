package gtclassic.item;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import gtclassic.GTMod;
import gtclassic.GTRecipes;
import gtclassic.util.GTItemColorInterface;
import gtclassic.util.GTValues;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.ILayeredItemModel;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemFile extends Item implements IStaticTexturedItem, GTItemColorInterface, ILayeredItemModel {

	String material;
	
	public GTItemFile(String material, Integer durability) {
		this.maxStackSize = 1;
		this.material = material;
		this.setMaxDamage(durability);
		setRegistryName(this.material+"_file");
		setUnlocalizedName(GTMod.MODID + "."+ this.material +"_file");
		setCreativeTab(GTMod.creativeTabGT);
		setRecipes();
	}
	
	public void setRecipes() {
		String input = "plate" + this.material;
		GTRecipes.recipes.addRecipe(new ItemStack(this, 1), new Object[] { "X  ", "X  ", "C  ", 'X', input, 'C', "stickWood" });
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack copy = itemStack.copy();
		return copy.attemptDamageItem(1, itemRand, null) ? ItemStack.EMPTY : copy;
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int meta) {
		return Ic2Icons.getTextures("gtclassic_items")[8];
	}
	
	@Override
	public Color getColor(ItemStack stack, int index) {
		if (index == 0){return GTValues.getColor("Wood");
		}
		else return GTValues.getColor(this.material);
	}

	@Override
	public boolean isLayered(ItemStack var1) {
		return true;
	}

	@Override
	public int getLayers(ItemStack var1) {
		return 2;
	}

	@Override
	public TextureAtlasSprite getTexture(int var1, ItemStack var2) {
		return Ic2Icons.getTextures(GTMod.MODID + "_materials")[8+var1];
	}

}
