package com.srsw.icfp2018.model.traceops;

import java.io.FileOutputStream;
import java.io.IOException;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;
import com.srsw.icfp2018.model.Voxel;

public class Fill extends Trace {

	public final Vector3 nd;
	
	public Fill(int i) {
		nd = decodeND(i);
	}
	
	public Fill(Vector3 v) {
		nd = v;
		// make sure it's valid
		if ((nd.x != 0) && (nd.y != 0) && (nd.z != 0)) {
			throw new RuntimeException("At least one axis must be zero: " + this);
		}
		if ((Math.abs(nd.x) > 1) || (Math.abs(nd.y) > 1) || (Math.abs(nd.z) > 1)) {
			throw new RuntimeException("nd out of range: " + this);
		}
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
	
	@Override
	public void write(FileOutputStream out) throws IOException {
		int i = ((nd.x + 1) * 9) + ((nd.y + 1) * 3) + (nd.z + 1);
		out.write((i << 3) | 0x03);
	}
}
