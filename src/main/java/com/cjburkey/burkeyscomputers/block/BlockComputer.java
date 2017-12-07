package com.cjburkey.burkeyscomputers.block;

import java.util.Map.Entry;
import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.computers.BaseComputer;
import com.cjburkey.burkeyscomputers.computers.ComputerHandler;
import com.cjburkey.burkeyscomputers.computers.ComputerOpener;
import com.cjburkey.burkeyscomputers.computers.WorldComputer;
import com.cjburkey.burkeyscomputers.tile.TileEntityComputer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockComputer extends Block implements ITileEntityProvider {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ON = PropertyBool.create("working");

	public BlockComputer() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(2.0f);
		setResistance(5.0f);
		setCreativeTab(CreativeTabs.REDSTONE);
		setHarvestLevel("pickaxe", 1);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer ply, EnumHand hand, EnumFacing facing, float x, float y, float z) {
		ComputerOpener.openForPlayer(ply, world, pos);
		return true;
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		linkComputerToTE(world, pos);
	}
	
	public void setProcessing(World world, BlockPos pos, IBlockState state, boolean processing) {
		world.setBlockState(pos, state.withProperty(ON, processing));
		linkComputerToTE(world, pos);
	}
	
	private void linkComputerToTE(World world, BlockPos pos) {
		if (!world.isRemote) {
			TileEntityComputer at = TileEntityComputer.getAt(world, pos);
			if (at == null) {
				return;
			}
			WorldComputer comp = getComputerAt(world, pos);
			if (comp == null) {
				comp = new WorldComputer(pos, world.provider.getDimension());
				ComputerHandler.get(world).addComputer(comp);
				ModLog.info("Created in-world computer with id: " + comp.getUniqueId());
			}
			at.setComputer(comp.getUniqueId());
		}
	}
	
	private WorldComputer getComputerAt(World world, BlockPos pos) {
		for (Entry<Long, BaseComputer> entry : ComputerHandler.get(world).getComputers()) {
			if (entry.getValue() instanceof WorldComputer) {
				WorldComputer bc = (WorldComputer) entry.getValue();
				if (bc.getWorld() == world.provider.getDimension() && pos.equals(bc.getPos())) {
					return bc;
				}
			}
		}
		return null;
	}
	
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			setDefaultFacing(world, pos, state.withProperty(ON, false));
		}
	}
	
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntityComputer ent = TileEntityComputer.getAt(world, pos);
			if (ent != null) {
				ComputerHandler.get(world).removeComputer(ent.getComputer());
				ModLog.info("Removed computer: " + ent.getComputer());
			}
		}
	}
	
	private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			IBlockState iblockstate = world.getBlockState(pos.north());
			IBlockState iblockstate1 = world.getBlockState(pos.south());
			IBlockState iblockstate2 = world.getBlockState(pos.west());
			IBlockState iblockstate3 = world.getBlockState(pos.east());
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}
			world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}
	
	public IBlockState getDefProp() {
		return getDefaultState().withProperty(ON, false);
	}
	
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float x, float y, float z, int data, EntityLivingBase player) {
		return getDefProp().withProperty(FACING, player.getHorizontalFacing().getOpposite()).withProperty(ON, false);
	}
	
	public IBlockState getStateFromMeta(int data) {
		EnumFacing facing = EnumFacing.getFront(data);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return getDefProp().withProperty(FACING, facing);
	}
	
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}
	
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation((EnumFacing) state.getValue(FACING)));
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ON });
	}
	
	public TileEntity createNewTileEntity(World world, int data) {
		return new TileEntityComputer();
	}
	
}