package com.cjburkey.burkeyscomputers.computers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.cjburkey.burkeyscomputers.ModInfo;
import com.cjburkey.burkeyscomputers.ModLog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class ComputerHandler extends WorldSavedData {
	
	private static final String DATA_NAME = ModInfo.MOD_ID + "-computers";
	private static ComputerHandler inst;
	
	private long currentId = 0;
	private Map<Long, BaseComputer> computers = new HashMap<>();
	
	public ComputerHandler() {
		this(DATA_NAME);
	}
	
	public ComputerHandler(String name) {
		super(name);
	}
	
	public void addComputer(BaseComputer comp) {
		if (comp == null) {
			return;
		}
		if (computers.containsKey(comp.getUniqueId())) {
			return;
		}
		comp.updateId(currentId);
		computers.put(currentId, comp);
		currentId ++;
		markDirty();
	}
	
	public void removeComputer(long comp) {
		if (comp < 0) {
			return;
		}
		if (!computers.containsKey(comp)) {
			return;
		}
		computers.remove(comp);
		markDirty();
	}

	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey("currentId") || !nbt.hasKey("computers")) {
			return;
		}
		NBTTagList computers = nbt.getTagList("computers", new NBTTagCompound().getId());
		ModLog.info("Loading computers: " + computers.tagCount());
		for (int i = 0; i < computers.tagCount(); i ++) {
			NBTTagCompound comp = computers.getCompoundTagAt(i);
			if (comp == null || !comp.hasKey("compLongIDe") || !comp.hasKey("extraData")) {
				continue;
			}
			long id = comp.getLong("compLongIDe");
			String data = comp.getString("extraData");
			BaseComputer cmpe = BaseComputer.loadFromData(data);
			cmpe.updateId(id);
			this.computers.put(id, cmpe);
		}
		currentId = nbt.getLong("currentId");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		NBTTagList tagList = new NBTTagList();
		for (Entry<Long, BaseComputer> computer : computers.entrySet()) {
			NBTTagCompound at = new NBTTagCompound();
			at.setLong("compLongIDe", computer.getKey());
			at.setString("extraData", computer.getValue().save());
			computer.getValue().writeToNBT(at);
			tagList.appendTag(at);
		}
		nbt.setLong("currentId", currentId);
		nbt.setTag("computers", tagList);
		return nbt;
	}
	
	public BaseComputer getComputer(long id) {
		return computers.get(id);
	}
	
	public static ComputerHandler get(World world) {
		if (inst != null) {
			return inst;
		}
		ModLog.info("Loading computer handler.");
		if (world == null) {
			return null;
		}
		MapStorage storage = world.getMapStorage();
		inst = (ComputerHandler) storage.getOrLoadData(ComputerHandler.class, DATA_NAME);
		if (inst == null) {
			inst = new ComputerHandler();
			storage.setData(DATA_NAME, inst);
		}
		return inst;
	}
	
}