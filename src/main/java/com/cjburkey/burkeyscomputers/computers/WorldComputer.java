package com.cjburkey.burkeyscomputers.computers;

import com.cjburkey.burkeyscomputers.ModLog;
import com.cjburkey.burkeyscomputers.block.BlockComputer;
import com.cjburkey.burkeyscomputers.block.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class WorldComputer extends BaseComputer {
	
	private BlockPos pos;
	private int world;
	private boolean previouslyWorking;
	
	public WorldComputer() {
		this(null, Integer.MIN_VALUE);
	}
	
	public WorldComputer(BlockPos pos, int world) {
		super(Integer.MIN_VALUE);
		this.pos = pos;
		this.world = world;
	}
	
	public void tick() {
		if (previouslyWorking == getProcessHandler().isEmpty()) {
			previouslyWorking = !getProcessHandler().isEmpty();
			World world = DimensionManager.getWorld(this.world);
			ModBlocks.blockComputer.setProcessing(world, pos, world.getBlockState(pos), previouslyWorking);
		}
		super.tick();
	}
	
	public static String getSaveIdentifier() {
		return "world";
	}
	
	public Object[] saveToString() {
		return new Object[] { world, pos.getX(), pos.getY(), pos.getZ() };
	}
	
	public void loadFromString(String[] data) {
		if (data.length != 4) {
			return;
		}
		try {
			int world = Integer.parseInt(data[0]);
			int x = Integer.parseInt(data[1]);
			int y = Integer.parseInt(data[2]);
			int z = Integer.parseInt(data[3]);
			this.world = world;
			this.pos = new BlockPos(x, y, z);
		} catch(Exception e) {
			ModLog.error("Failed to parse: \"" + data + "\"");
		}
	}
	
	public int getWorld() {
		return world;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
}