package com.srsw.icfp2018.model;

import java.util.Set;
import java.util.TreeSet;

public class Bot implements Comparable<Bot> {
	public int bid;
	public Vector3 pos;
	public Set<Integer> seeds;
	public Trace nextOp;
	public State state;
	
	@Override
	public int compareTo(Bot bot) {
		return this.bid - bot.bid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bot) {
			return false;
		} else {
			Bot bot = (Bot) obj;
			return this.bid == bot.bid;
		}
	}
	
	@Override
	public String toString() {
		return bid + ": " + pos + " " + seeds;
	}
	
	
	// should only be used for initial Bot
	public Bot(State state) {
		bid = 1;
		pos = new Vector3(0, 0, 0);
		seeds = new TreeSet<>();
		for (int i = 2; i <= 20; i++) {
			seeds.add(i);
		}
		this.state = state;
	}

	public void execute() {
		System.out.println(">> bot " + bid + " exec: " + nextOp);
		nextOp.execute(this);
		nextOp = null;
	}
}
