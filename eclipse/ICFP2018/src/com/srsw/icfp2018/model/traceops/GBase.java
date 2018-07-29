package com.srsw.icfp2018.model.traceops;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Region;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class GBase extends Trace {

	public final Vector3 nd;
	public final Vector3 fd;
	
	protected GBase(int nd, InputStream in) throws TraceFileException {
		this.nd = decodeND(nd);
		int x;
		try {
			x = in.read();
			int y = in.read();
			int z = in.read();
			if ((x == -1) || (y == -1) || (z == -1)) {
				throw new EOFException();
			}
			if ((x > 60) || (y > 60) || (z > 60)) {
				throw new TraceFileException("invalid fd in " + getClass() + ": " + x + "," + y + "," + z);
			}
			fd = new Vector3(x-30, y-30, z-30);
		} catch (IOException e) {
			throw new TraceFileException("Error reading fd bytes of " + getClass(), e);
		}
	}
	
	protected GBase(Vector3 nd, Vector3 fd) {
		assertND(nd);
		assertFD(fd);
		this.nd = nd;
		this.fd = fd;
	}

	
	protected Region executeFirst(Bot bot) {
		State state = bot.state;
		
		int axes = 0;
		if (fd.x != 0)	axes++;
		if (fd.y != 0)	axes++;
		if (fd.z != 0)	axes++;
		int nOtherBots = (1 << axes) - 1;
		System.out.println("GFill: " + axes + " axes, " + nOtherBots + " other bots");
		
		
		// make sure there are other bots at the opposite corner(s)
		Region region = getRegion(bot);
		System.out.println("  region = " + region);
		
		ArrayList<Bot> otherbots = new ArrayList<>();
		
		for (Bot otherbot : state.bots) {
			if (otherbot.equals(bot)) {
				continue;
			}
			System.out.println("  Otherbot: " + otherbot.bid + "; move=" + otherbot.nextOp);
			if (otherbot.nextOp.getClass().equals(getClass())) {
				GBase otherOp = (GBase) otherbot.nextOp;
				Region otherRegion = otherOp.getRegion(otherbot);
				System.out.println("    region = " + otherRegion + "; " 
						+ (region.equals(otherRegion) ? "MATCH" : "no match"));
				if (region.equals(otherRegion)) {
					otherbots.add(otherbot);
				}
			}
		}
		if (otherbots.size() != nOtherBots) {
			throw new RuntimeException("GFill: wrong number of otherbots: expected=" + nOtherBots + ", actual=" + otherbots.size());
		}
		for (Bot otherbot : otherbots) {
			otherbot.nextOp = new Nop(otherbot.nextOp.toString());
		}
		return region;
	}
	
	protected Region getRegion(Bot bot) {
		Vector3 nearCorner = new Vector3(bot.pos);
		nearCorner.move(nd);
		Vector3 farCorner = new Vector3(nearCorner);
		farCorner.move(fd);		
		return new Region(nearCorner, farCorner);
	}
}
