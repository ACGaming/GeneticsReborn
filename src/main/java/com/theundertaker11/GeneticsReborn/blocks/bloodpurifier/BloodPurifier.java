package com.theundertaker11.GeneticsReborn.blocks.bloodpurifier;

import javax.annotation.Nullable;

import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.blocks.StorageBlockBase;
import com.theundertaker11.GeneticsReborn.blocks.dnaextractor.DNAExtractor;
import com.theundertaker11.GeneticsReborn.gui.GuiHandler;
import com.theundertaker11.GeneticsReborn.items.GRItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BloodPurifier extends StorageBlockBase{
	
	public BloodPurifier(String name) {
		super(name);
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new GRTileEntityBloodPurifier();
    }
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) return true;
		TileEntity tEntity = worldIn.getTileEntity(pos);
		
		if(tEntity!=null&&tEntity instanceof GRTileEntityBloodPurifier&&hand==EnumHand.MAIN_HAND)
		{
			if(playerIn.getHeldItem(EnumHand.MAIN_HAND)!=null&&playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem()==GRItems.Overclocker)
			{
				GRTileEntityBloodPurifier tile = (GRTileEntityBloodPurifier)tEntity;
				tile.addOverclocker(playerIn, 5);
			}
			else playerIn.openGui(GeneticsReborn.instance, GuiHandler.BloodPurifierGuiID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        //All the null checks are just in case and probably not actually needed
        if (tile!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)!=null
        	&&tile instanceof GRTileEntityBloodPurifier&&tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
        {
        	GRTileEntityBloodPurifier tileentity = (GRTileEntityBloodPurifier)tile;
        	IItemHandler input = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        	IItemHandler output = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        	if(input.getStackInSlot(0)!=null)
        	{
        		ItemStack inputstack = input.getStackInSlot(0);
        		EntityItem entityinput = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), inputstack);
        		tileentity.getWorld().spawnEntityInWorld(entityinput);
        	}
        	if(output.getStackInSlot(0)!=null)
        	{
        		ItemStack outputstack = output.getStackInSlot(0);
        		EntityItem entityoutput = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), outputstack);
        		tileentity.getWorld().spawnEntityInWorld(entityoutput);
        	}
        	if(tileentity.getOverclockerCount()>0)
        	{
            	ItemStack overclockers = new  ItemStack(GRItems.Overclocker, tileentity.getOverclockerCount());
            	EntityItem entityoverclockers = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), overclockers);
        		tileentity.getWorld().spawnEntityInWorld(entityoverclockers);
        	}
        }
        super.breakBlock(worldIn, pos, state);
    }
}
