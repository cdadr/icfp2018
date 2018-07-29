package com.srsw.icfp2018.model.traceops;

import java.util.Collections;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;

public abstract class FusionBase extends Trace {

	public final Vector3 nd;
	
	protected FusionBase(int i) {
		nd = decodeND(i);
	}
	
	protected FusionBase(Vector3 nd) {
		assertND(nd);
		this.nd = nd;
	}
	
	private Vector3 targetPos(Bot bot) {
		Vector3 v = new Vector3(bot.pos);
		v.move(nd);
		return v;
	}

	protected void executeFusion(Bot bot, boolean isPrimary) throws ModelRuntimeException {
		State state = bot.state;
		Class<? extends FusionBase> otherClass = isPrimary ? FusionS.class : FusionP.class;
		Vector3 otherbotPos = targetPos(bot);
		
		System.out.println("Looking for otherbot at " + otherbotPos);
		for (Bot otherbot : state.bots) {
			if (otherbot.equals(bot)) {
				continue;
			}
			if (!otherbot.pos.equals(otherbotPos)) {
				continue;
			}
			System.out.println("Found otherbot: " + otherbot.bid + "; move=" + otherbot.nextOp + " at " + otherbotPos);
			if ((otherbot.nextOp == null) || (otherbot.nextOp.getClass() != otherClass)) {
				throw new ModelRuntimeException("expected op " + otherClass.getSimpleName() + "; otherbot at " + otherbotPos + " op=" + otherbot.nextOp);
			}
			FusionBase otherOp = (FusionBase) otherbot.nextOp;
			Vector3 thisbotPos = otherOp.targetPos(otherbot);
			if (!bot.pos.equals(thisbotPos)) {
				throw new ModelRuntimeException("otherbot fusion location mismatch: this=" + bot.pos + "; otherbot: " + otherbot); 
			}
			
			Bot keepBot, killBot;
			if (isPrimary) {
				keepBot = bot;
				killBot = otherbot;
			} else {
				keepBot = otherbot;
				killBot = bot;
			}
			keepBot.seeds.add(killBot.bid);
			keepBot.seeds.addAll(killBot.seeds);
			Collections.sort(keepBot.seeds);
			
			// kill bot
			state.bots.remove(killBot);
			
			// otherbot doesn't need to do anything
			otherbot.nextOp = new Nop(otherbot.nextOp.toString());
			
			System.out.println("keepBot: " + keepBot);
			
			state.energy -= 24;
		}
	}

}
