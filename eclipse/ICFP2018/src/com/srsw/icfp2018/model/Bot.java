package com.srsw.icfp2018.model;

import java.util.ArrayList;
import java.util.List;

public class Bot implements Comparable<Bot> {
	public int bid;
	public Vector3 pos;
	public List<Integer> seeds;
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
		seeds = new ArrayList<>();
		for (int i = 2; i <= 20; i++) {
			seeds.add(i);
		}
		this.state = state;
	}
	
	public Bot(Bot sourceBot, Vector3 nd, int m) {
		bid = sourceBot.seeds.get(0);
		pos = new Vector3(sourceBot.pos);
		pos.move(nd);
		seeds = new ArrayList<>();
		if (m > 0) {
			seeds.addAll(sourceBot.seeds.subList(1, m + 1));
		}
		state = sourceBot.state;
	}

	public void execute() {
		System.out.println(">> bot " + bid + " exec: " + nextOp);
		nextOp.execute(this);
		nextOp = null;
	}
}
