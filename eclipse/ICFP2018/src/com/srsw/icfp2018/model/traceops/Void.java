package com.srsw.icfp2018.model.traceops;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;

public class Void  extends Trace {

	public final Vector3 nd;
	
	public Void(int i) {
		nd = decodeND(i);
	}
	
	public Void(Vector3 nd) {
		assertND(nd);
		this.nd = nd;
	}


	@Override
	public String toString() {
		return "Void " + nd;
	}
	
	@Override
	public void execute(Bot bot) {
		State state = bot.state;
		Vector3 pos = new Vector3(bot.pos);
		pos.move(nd);
		if (state.matrix.get(pos) == null) {
			// already clear; do nothing, but it still takes energy
			state.energy += 3;
		} else {
			state.matrix.clear(pos);
			state.energy -= 12;		// convert matter->energy!
		}
	}
}
