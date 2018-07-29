package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Region;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class GVoid extends GBase {

	public GVoid(int nd, InputStream in) throws TraceFileException {
		super(nd, in);
	}

	public GVoid(Vector3 nd, Vector3 fd) {
		super(nd, fd);
	}

	@Override
	public String toString() {
		return "GVoid " + nd + " " + fd;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		int i = ((nd.x + 1) * 9) + ((nd.y + 1) * 3) + (nd.z + 1);
		out.write((i << 3) | 0x00);
		out.write(fd.x + 30);
		out.write(fd.y + 30);
		out.write(fd.z + 30);
	}
	
	@Override
	public void execute(Bot bot) {
		Region region = executeFirst(bot);	

		// clear region
		State state = bot.state;
		for (int x = region.cornerL.x; x <= region.cornerH.x; x++) {
			for (int y = region.cornerL.y; y <= region.cornerH.y; y++) {
				for (int z = region.cornerL.z; z <= region.cornerH.z; z++) {
					if (state.matrix.get(x, y, z) == null) {
						// already clear; do nothing, but it still takes energy
						state.energy += 3;
					} else {
						state.matrix.clear(x, y, z);
						state.energy -= 12;		// convert matter->energy!
					}
				}
			}
		}
	}
}
