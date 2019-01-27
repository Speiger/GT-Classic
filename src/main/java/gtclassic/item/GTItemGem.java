package gtclassic.item;

import java.util.Arrays;
import java.util.List;

import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTItemGem extends Item implements IStaticTexturedItem {
	public enum GTItemGemTypes {
		RUBY(17), SAPPHIRE(18);

		private int id;

		GTItemGemTypes(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}
	}

	GTItemGemTypes variant;

	public GTItemGem(GTItemGemTypes variant) {
		this.variant = variant;
		setRegistryName(variant.toString().toLowerCase() + "_gem");
		setUnlocalizedName(GTMod.MODID + "." + variant.toString().toLowerCase() + "_gem");
		setCreativeTab(GTMod.creativeTabGT);
	}

	@Override
	public List<Integer> getValidVariants() {
		return Arrays.asList(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture(int i) {
		return Ic2Icons.getTextures(GTMod.MODID + "_items")[variant.getID()];
	}
}
