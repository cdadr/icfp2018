package com.srsw.icfp2018.model;

import java.util.ArrayList;
import java.util.List;

public class Bot implements Comparable<Bot> {
	public int bid;
	public Vector3 pos;
	public List<Integer> seeds;
	public Trace nextOp;
	public State state;
	
	private static final int NUM_BOTS = 40;		// full
//	private static final int NUM_BOTS = 20;		// lightning
	
	
	
	// should only be used for initial Bot
	public Bot(State state) {
		bid = 1;
		pos = new Vector3(0, 0, 0);
		seeds = new ArrayList<>();
		for (int i = 2; i <= NUM_BOTS; i++) {
			seeds.add(i);
		}
		this.state = state;
	}
	
	// called by fission op
	public Bot(Bot sourceBot, Vector3 nd, int m) throws ModelRuntimeException {
		bid = sourceBot.seeds.get(0);
		pos = new Vector3(sourceBot.pos);
		pos.move(nd);
		seeds = new ArrayList<>();
		if (m > 0) {
			seeds.addAll(sourceBot.seeds.subList(1, m + 1));
		}
		state = sourceBot.state;
		pos.validate(state.r);
	}

	@Override
	public int compareTo(Bot bot) {
		return this.bid - bot.bid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bot) {
			Bot bot = (Bot) obj;
			return this.bid == bot.bid;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
//		return bid + ": " + pos + " " + seeds;
		StringBuilder sb = new StringBuilder();
		int nSeeds = seeds.size();
		sb.append(bid).append(": ").append(pos).append(" ").append(nSeeds);
		if (nSeeds == 0) {
			sb.append("[]");
		} else if (nSeeds < 4) {
			sb.append(seeds);
		} else {
			sb.append("[")
				.append(seeds.get(0)).append(",")
				.append(seeds.get(1)).append(",")
				.append(seeds.get(2)).append(",...]");
		}
		return sb.toString();
	}
	
	public void execute() throws ModelRuntimeException {
		System.out.println(">> bot " + bid + " exec: " + nextOp);
		nextOp.execute(this);
		nextOp = null;
	}
}
