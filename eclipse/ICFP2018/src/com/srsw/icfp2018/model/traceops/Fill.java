package com.srsw.icfp2018.model.traceops;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;
import com.srsw.icfp2018.model.Voxel;

public class Fill extends Trace {

	public Vector3 nd;
	
	public Fill(int i) {
		nd = decodeND(i);
	}
	
	@Override
	public String toString() {
		return "Fill " + nd;
	}
	
	
	@Override
	public void execute(Bot bot) {
		State state = bot.state;
		Voxel voxel = new Voxel(bot.pos, nd);
		if (state.matrix.get(voxel.pos) == null) {
			state.matrix.put(voxel.pos, voxel);
			state.energy += 12;
		} else {
			// already something there; do nothing, but it still takes energy
			state.energy += 6;
		}
	}
}
