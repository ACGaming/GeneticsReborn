package com.theundertaker11.GeneticsReborn.crafting;

import com.theundertaker11.GeneticsReborn.items.GRItems;
import com.theundertaker11.GeneticsReborn.util.ModUtils;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class AntiPlasmidCrafting implements IRecipe {
	// 012
	// 345
	// 678
@Override
public boolean matches(InventoryCrafting inv, World worldIn) 
{
	int SmallGridEmptySpaces = 0;
	int SmallGridPlasmid = 0;
	int SmallGridAntiPlasmid = 0;
	int GridEmptySpaces = 0;
	int GridPlasmid = 0;
	int GridAntiPlasmid = 0;
	if(inv.getSizeInventory()==4)
	{
		for(int i=0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack item = inv.getStackInSlot(i);
			if(item!=null)
			{
				if(item.getItem()==GRItems.AntiPlasmid)
				{
					SmallGridAntiPlasmid++;
				}
				if(item.getItem()==GRItems.Plasmid&&item.getTagCompound()!=null)
				{
					if(item.getTagCompound().getInteger("num")==item.getTagCompound().getInteger("numNeeded")) SmallGridPlasmid++;
					else return false;
				}
			}else SmallGridEmptySpaces++;
		}
	}
	else
	{
		for(int i=0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack item = inv.getStackInSlot(i);
			if(item!=null)
			{
				if(item.getItem()==GRItems.AntiPlasmid)
				{
					GridAntiPlasmid++;
				}
				if(item.getItem()==GRItems.Plasmid&&item.getTagCompound()!=null)
				{
					if(item.getTagCompound().getInteger("num")==item.getTagCompound().getInteger("numNeeded")) GridPlasmid++;
					else return false;
				}
			}else GridEmptySpaces++;
		}
	}
	if(SmallGridEmptySpaces==2&&SmallGridPlasmid==1&&SmallGridAntiPlasmid==1)
	{
		return true;
	}
	if(GridEmptySpaces==7&&GridPlasmid==1&&GridAntiPlasmid==1)
	{
		return true;
	}
	return false;
}

@Override
public ItemStack getCraftingResult(InventoryCrafting inv) 
{
	ItemStack result = new ItemStack(GRItems.AntiPlasmid);
	for(int i=0; i < inv.getSizeInventory(); ++i)
	{
		ItemStack stack = inv.getStackInSlot(i);
		if(stack!=null&&stack.getItem()==GRItems.Plasmid&&stack.getTagCompound()!=null)
		{
			ModUtils.getTagCompound(result).setString("gene", stack.getTagCompound().getString("gene"));
			ModUtils.getTagCompound(result).setInteger("num", stack.getTagCompound().getInteger("num"));
			ModUtils.getTagCompound(result).setInteger("numNeeded", stack.getTagCompound().getInteger("numNeeded"));
			return result;
		}
	}
	return null;
}

@Override
public int getRecipeSize() 
{
	return 1;
}

@Override
public ItemStack getRecipeOutput() 
{
	return new ItemStack(GRItems.AntiPlasmid);
}

@Override
public ItemStack[] getRemainingItems(InventoryCrafting inv) 
{
	return new ItemStack[inv.getSizeInventory()];
}

}
