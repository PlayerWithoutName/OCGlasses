package com.bymarcin.openglasses.block;

import com.bymarcin.openglasses.OpenGlasses;
import com.bymarcin.openglasses.item.OpenGlassesItem;
import com.bymarcin.openglasses.network.packet.WidgetUpdatePacket;
import com.bymarcin.openglasses.surface.ServerSurface;
import com.bymarcin.openglasses.tileentity.OpenGlassesTerminalTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OpenGlassesTerminalBlock extends BlockContainer {

	public OpenGlassesTerminalBlock() {
		super(Material.IRON);
		setCreativeTab(OpenGlasses.creativeTab);
		setRegistryName("openglassesterminal");
		setHardness(3.0F);
		setUnlocalizedName("openglassesterminal");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new OpenGlassesTerminalTileEntity();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTileEntity(IBlockAccess world, BlockPos pos, Class<T> T) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && T.isAssignableFrom(te.getClass())) {
			return (T) te;
		}
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking() || world.isRemote)
			return false;
		OpenGlassesTerminalTileEntity te = getTileEntity(world, pos, OpenGlassesTerminalTileEntity.class);
		if (te == null)
			return false;
		ItemStack glassesStack = player.getHeldItemMainhand();
		if (glassesStack != null) {
			Item item = glassesStack.getItem();
			if (item instanceof OpenGlassesItem) {
				((OpenGlassesItem) item).bindToTerminal(glassesStack, te.getTerminalUUID());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		onBlockPreDestroy(worldIn, pos);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		onBlockPreDestroy(worldIn, pos);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		onBlockPreDestroy(worldIn, pos);
	}


	public void onBlockPreDestroy(World world, BlockPos pos) {
		OpenGlassesTerminalTileEntity te = getTileEntity(world, pos, OpenGlassesTerminalTileEntity.class);
		if (te != null)
			ServerSurface.instance.sendToUUID(new WidgetUpdatePacket(), te.getTerminalUUID());
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
