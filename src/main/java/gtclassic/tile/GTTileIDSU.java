package gtclassic.tile;

import java.util.UUID;

import gtclassic.container.GTContainerIDSU;
import gtclassic.util.energy.IDSUStorage;
import gtclassic.util.energy.IDSUStorage.EnergyWrapper;
import ic2.api.classic.network.adv.NetworkField;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.block.base.tile.TileEntityElectricBlock;
import ic2.core.inventory.container.ContainerIC2;
import ic2.core.platform.lang.components.base.LangComponentHolder.LocaleBlockComp;
import ic2.core.platform.registry.Ic2Items;
import ic2.core.platform.lang.components.base.LocaleComp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLLog;

public class GTTileIDSU extends TileEntityElectricBlock {

	int open = 0;
	
	@NetworkField(index = 7)
	private UUID owner;
	
	@NetworkField(index = 8)
	EnergyWrapper wrapper = IDSUStorage.createDummy();
	
	public GTTileIDSU() {
		super(4, (int) EnergyNet.instance.getPowerFromTier(4), 400000000);
		this.addNetworkFields(new String[] { "owner" });
		addGuiFields("wrapper");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasUniqueId("owner")) {
			this.owner = nbt.getUniqueId("owner");
			this.getNetwork().updateTileGuiField(this, "owner");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.owner != null) {
			nbt.setUniqueId("owner", this.owner);
		}
		return nbt;
	}

	@Override
	public ContainerIC2 getGuiContainer(EntityPlayer player) {
		if(isSimulating())
		{
			open++;
		}
		return new GTContainerIDSU(player.inventory, this);
	}
	
	@Override
	public void onGuiClosed(EntityPlayer player)
	{
		super.onGuiClosed(player);
		if(isSimulating())
		{
			open--;
		}
	}
	
	@Override
	public int getProcessRate() {
		return 128;
	}

	@Override
	public double getWrenchDropRate() {
		return 0.70D;
	}

	@Override
	public void onLoaded() {
		super.onLoaded();
		if(owner != null && isSimulating())
		{
			wrapper = IC2.saveManager.getGlobalData(getWorld(), "IDSU_Storage", IDSUStorage.class).getWrapper(owner);
		}
	}
	
	@Override
	public void update()
	{
		if(owner != null && isSimulating() && wrapper.isDummy())
		{
			wrapper = IC2.saveManager.getGlobalData(getWorld(), "IDSU_Storage", IDSUStorage.class).getWrapper(owner);
		}
		if(wrapper.isDummy())
		{
			return;
		}
		if(open > 0)
		{
			getNetwork().updateTileGuiField(this, "wrapper");	
		}
		handleRedstone();
		boolean isEmitting = isEmittingRedstone();
		if(wrapper.getStoredEnergy() > 0 && !inventory.get(0).isEmpty())
		{
			wrapper.removeEnergy((int)ElectricItem.manager.charge(inventory.get(0), wrapper.getStoredEnergy(), tier, false, false));
		}
		if(maxEnergy > wrapper.getStoredEnergy() && !inventory.get(1).isEmpty())
		{
			ItemStack fuel = inventory.get(1);
			Item item = fuel.getItem();
			boolean removeItem = false;
			if(item == Items.REDSTONE)
			{
				wrapper.addEnergy(500);
				removeItem = true;
			}
			else if(item == Item.getItemFromBlock(Blocks.REDSTONE_BLOCK))
			{
				wrapper.addEnergy(5000);
				removeItem = true;
			}
			else if(item == Ic2Items.suBattery.getItem())
			{
				wrapper.addEnergy(1000);
				removeItem = true;
			}
			else
			{
				wrapper.addEnergy((int)ElectricItem.manager.discharge(fuel, (maxEnergy - wrapper.getStoredEnergy()), tier, false, true, false));
			}
			if(wrapper.getStoredEnergy() > maxEnergy)
			{
				wrapper.setEnergy(maxEnergy);
			}
			if(removeItem)
			{
				fuel.shrink(1);
			}
		}
		int newState = getUpdatedState();
		if(newState != state)
		{
			state = newState;
			getNetwork().updateTileEntityField(this, "state");
		}
		boolean newEmitting = isEmittingRedstone();
		if(isEmitting != newEmitting || emit != newEmitting)
		{
			updateNeighbors(true);
			emit = newEmitting;
		}
		updateComparators();
	}

	@Override
	public LocaleComp getBlockName() {
		return new LocaleBlockComp(this.getBlockType().getUnlocalizedName());
	}

	public void setOwner(UUID user) {
		if (this.owner == null && user != null) {
			this.owner = user;
		}
		this.getNetwork().updateTileGuiField(this, "owner");
	}

	public UUID getOwner() {
		return this.owner;
	}
	
	@Override
	public double getOfferedEnergy()
	{
		if(wrapper.isDummy())
		{
			return 0;
		}
		if(wrapper.getStoredEnergy() < output)
		{
			return 0;
		}
		if(redstoneMode == 6 && isRedstonePowered())
		{
			return 0;
		}
		if(redstoneMode == 7 && isRedstonePowered() && wrapper.getStoredEnergy() < maxEnergy)
		{
			return 0;
		}
		return output;
	}
	
	@Override
	public void drawEnergy(double amount)
	{
		if(wrapper.isDummy())
		{
			return;
		}
		wrapper.removeEnergy((int)amount);
		getNetwork().updateTileGuiField(this, "wrapper");
	}
	
	@Override
	public double getDemandedEnergy()
	{
		if(wrapper.isDummy())
		{
			return 0D;
		}
		return this.maxEnergy - wrapper.getStoredEnergy();
	}
	
	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage)
	{
		if(amount > output || amount <= 0 || wrapper.isDummy())
		{
			return 0;
		}
		wrapper.addEnergy((int)amount);
		int left = 0;
		if(wrapper.getStoredEnergy() >= maxEnergy)
		{
			left = wrapper.getStoredEnergy() - maxEnergy;
			wrapper.setEnergy(maxEnergy);
			getNetwork().updateTileGuiField(this, "wrapper");
		}
		return left;
	}
	
	@Override
	public int getStoredEU()
	{
		return wrapper.getStoredEnergy();
	}
	
	@Override
	public int getStored()
	{
		return wrapper.getStoredEnergy();
	}
	
	@Override
	public int getStoredEnergy()
	{
		return wrapper.getStoredEnergy();
	}
	
	public float getChargeLevel()
	{
		float ret = (float)this.wrapper.getStoredEnergy() / (float)maxEnergy;
		if(ret > 1.0f)
		{
			ret = 1.0f;
		}
		return ret;
	}
	
	@Override
	public void setStored(int newEnergy)
	{
		if(wrapper.isDummy())
		{
			return;
		}
		wrapper.setEnergy(newEnergy);
	}
	
	@Override
	public int addEnergy(int amount)
	{
		if(wrapper.isDummy())
		{
			return 0;
		}
		wrapper.addEnergy(amount);
		return wrapper.getStoredEnergy();
	}
	
	@Override
	public void addPower(int amount)
	{
		if(wrapper.isDummy())
		{
			return;
		}
		wrapper.addEnergy(amount);
	}
	
	@Override
	public void drawPower(int amount)
	{
		if(wrapper.isDummy())
		{
			return;
		}
		wrapper.removeEnergy(amount);
	}
}
