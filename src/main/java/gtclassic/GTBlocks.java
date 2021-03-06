package gtclassic;

import java.util.ArrayList;
import java.util.List;

import gtclassic.block.GTBlockCasing;
import gtclassic.block.GTBlockMachine;
import gtclassic.block.GTBlockMachineDirectionable;
import gtclassic.block.GTBlockOre;
import gtclassic.block.GTBlockQuantumChest;
import gtclassic.color.GTColorBlockInterface;
import gtclassic.color.GTColorItemBlock;
import gtclassic.itemblock.GTItemBlockInterface;
import gtclassic.itemblock.GTItemBlockRare;
import gtclassic.material.GTMaterialGen;
import gtclassic.tile.GTTileAESU;
import gtclassic.tile.GTTileBufferLarge;
import gtclassic.tile.GTTileBufferSmall;
import gtclassic.tile.GTTileCabinet;
import gtclassic.tile.GTTileCentrifuge;
import gtclassic.tile.GTTileChargeOMat;
import gtclassic.tile.GTTileComputerCube;
import gtclassic.tile.GTTileFacing;
import gtclassic.tile.GTTileIDSU;
import gtclassic.tile.GTTileMatterFabricator;
import gtclassic.tile.GTTilePlayerDetector;
import gtclassic.tile.GTTileQuantumChest;
import gtclassic.tile.GTTileSluice;
import gtclassic.tile.GTTileTranslocator;
import gtclassic.tile.GTTileWorktable;
import gtclassic.tile.multi.GTTileMultiBlastFurnace;
import gtclassic.tile.multi.GTTileMultiFusion;
import gtclassic.tile.multi.GTTileMultiLightningRod;
import ic2.core.IC2;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GTBlocks {

	private GTBlocks() {
		throw new IllegalStateException("Utility class");
	}

	static final List<Block> toRegister = new ArrayList<>();
	public static final GTBlockOre oreEnd = registerBlock(new GTBlockOre("End", 80, 20.0F, 3));
	public static final GTBlockOre oreIridium = registerBlock(new GTBlockOre("Iridium", 81, 20.0F, 3));
	public static final GTBlockOre oreRuby = registerBlock(new GTBlockOre("Ruby", 82, 4.0F, 2));
	public static final GTBlockOre oreSapphire = registerBlock(new GTBlockOre("Sapphire", 83, 4.0F, 2));
	public static final GTBlockOre oreBauxite = registerBlock(new GTBlockOre("Bauxite", 84, 3.0F, 1));
	public static final GTBlockCasing casingFusion = registerBlock(new GTBlockCasing("fusion", 2));
	public static final GTBlockCasing casingLapotron = registerBlock(new GTBlockCasing("lapotron", 5));
	public static final GTBlockCasing casingReinforced = registerBlock(new GTBlockCasing("reinforced", 1));
	public static final GTBlockCasing casingHighlyAdvanced = registerBlock(new GTBlockCasing("highlyadvanced", 29));
	// public static final GTBlockSluice tileSluice = registerBlock(new
	// GTBlockSluice());
	// public static final GTBlockSluiceBoxExt tileSluiceExt = registerBlock(new
	// GTBlockSluiceBoxExt());
	public static final GTBlockMachine tileBlastFurnace = registerBlock(new GTBlockMachine("gtblastfurnace", 3));
	public static final GTBlockMachine tileChargeOmat = registerBlock(new GTBlockMachine("chargeomat"));
	public static final GTBlockMachine tileComputer = registerBlock(new GTBlockMachine("computercube"));
	public static final GTBlockMachine tileCentrifuge = registerBlock(new GTBlockMachine("industrialcentrifuge"));
	public static final GTBlockMachine tileFabricator = registerBlock(new GTBlockMachine("matterfabricator"));
	public static final GTBlockMachine tileUUAssembler = registerBlock(new GTBlockMachine("uuassembler"));
	public static final GTBlockMachine tilePlayerDetector = registerBlock(new GTBlockMachine("playerdetector", 1));
	public static final GTBlockMachine tileFusionComputer = registerBlock(new GTBlockMachine("fusioncomputer", 4));
	public static final GTBlockMachine tileLightningRod = registerBlock(new GTBlockMachine("lightningrod", 2));
	public static final GTBlockMachineDirectionable tileLESU = registerBlock(new GTBlockMachineDirectionable("lesu"));
	public static final GTBlockMachineDirectionable tileAESU = registerBlock(new GTBlockMachineDirectionable("aesu"));
	public static final GTBlockMachineDirectionable tileIDSU = registerBlock(new GTBlockMachineDirectionable("idsu"));
	public static final GTBlockMachine tileWorktable = registerBlock(new GTBlockMachine("worktable"));
	public static final GTBlockMachine tileCabinet = registerBlock(new GTBlockMachine("cabinet"));
	public static final GTBlockQuantumChest tileQuantumChest = registerBlock(new GTBlockQuantumChest());
	public static final GTBlockMachineDirectionable tileTranslocator = registerBlock(new GTBlockMachineDirectionable("translocator", 3));
	public static final GTBlockMachineDirectionable tileBufferLarge = registerBlock(new GTBlockMachineDirectionable("bufferlarge", 2));
	public static final GTBlockMachineDirectionable tileBufferSmall = registerBlock(new GTBlockMachineDirectionable("buffersmall", 2));
	/** This is where GTBlockTile holds its textures **/
	protected static final String[] textureTileBasic = { "gtblastfurnace", "chargeomat", "computercube",
			"industrialcentrifuge", "matterfabricator", "uuassembler", "quantumchest", "playerdetector",
			"fusioncomputer", "lightningrod", "idsu", "aesu", "lesu", "cabinet", "worktable", "translocator",
			"bufferlarge", "buffersmall" };

	public static void registerBlocks() {
		for (Block block : GTMaterialGen.blockMap.values()) {
			createBlock(block);
		}
		for (Block block : toRegister) {
			createBlock(block);
		}
	}

	static <T extends Block> T registerBlock(T block) {
		toRegister.add(block);
		return block;
	}

	public static void createBlock(Block block) {
		IC2.getInstance().createBlock(block, getItemBlock(block));
	}

	static Class<? extends ItemBlockRare> getItemBlock(Block block) {
		if (block instanceof GTItemBlockInterface) {
			return ((GTItemBlockInterface) block).getCustomItemBlock();
		}
		if (block instanceof GTColorBlockInterface) {
			return GTColorItemBlock.class;
		}
		return GTItemBlockRare.class;
	}

	public static void registerTiles() {
		registerUtil(GTTileSluice.class, "Sluice");
		registerUtil(GTTileCentrifuge.class, "IndustrialCentrifuge");
		registerUtil(GTTilePlayerDetector.class, "PlayerDetector");
		registerUtil(GTTileComputerCube.class, "ComputerCube");
		registerUtil(GTTileMultiBlastFurnace.class, "BlastFurnace");
		registerUtil(GTTileChargeOMat.class, "ChargeOMat");
		registerUtil(GTTileIDSU.class, "IDSU");
		registerUtil(GTTileAESU.class, "AESU");
		registerUtil(GTTileMultiLightningRod.class, "LightningRod");
		registerUtil(GTTileMultiFusion.class, "FusionComputer");
		registerUtil(GTTileQuantumChest.class, "QuantumChest");
		registerUtil(GTTileMatterFabricator.class, "MatterFabricator");
		registerUtil(GTTileWorktable.class, "Worktable");
		registerUtil(GTTileFacing.class, "Facing");
		registerUtil(GTTileCabinet.class, "Cabinet");
		registerUtil(GTTileTranslocator.class, "Translocator");
		registerUtil(GTTileBufferSmall.class, "BufferSmall");
		registerUtil(GTTileBufferLarge.class, "BufferLarge");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerUtil(Class tile, String name) {
		GameRegistry.registerTileEntity(tile, new ResourceLocation(GTMod.MODID, "tileEntity" + name));
	}
}