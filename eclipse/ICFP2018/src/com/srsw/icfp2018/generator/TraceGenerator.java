package com.srsw.icfp2018.generator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.srsw.icfp2018.model.Model;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;
import com.srsw.icfp2018.model.traceops.SMove;

public class TraceGenerator {
	
	public enum Mode { Add, Delete, Replace };

	Model srcModel;
	Model tgtModel;
	Mode mode;
	public List<Trace> trace = new ArrayList<>();
	
	public TraceGenerator(Model srcModel, Model tgtModel, Mode mode) {
		this.srcModel = srcModel;
		this.tgtModel = tgtModel;
		this.mode = mode;
	}

	public void run() {
		switch (mode) {
		case Add:
			runAdd();
			break;
		case Delete:
			runDelete();
			break;
		case Replace:
			runReplace();
			break;
		}
	}

	// Essentially the same as the default, but with some optimizations.
	// Be more conservative with the bbox, use longer SMove ops.
	// Still single bot, and only filling voxel at y-1.
	private void runAdd() {
		Vector3 currentPos = new Vector3(0, 0, 0);
		Vector3 bboxL = new Vector3(0, 0, 0);
		Vector3 bboxH = new Vector3(0, 0, 0);
		
		for (int y = 0; y < tgtModel.r; y++) {
			if (y == 1) {
				// only flip once we've written the base layer
				trace.add(Trace.flip);
			}
			System.out.println("Current pos: " + currentPos);
			if (!tgtModel.getPlaneBBox(y, bboxL, bboxH)) {
				System.out.println("no cells on plane " + y);
				continue;
			}
			System.out.println("plane " + y + " bbox: " + bboxL + " --> " + bboxH);
			int distL = currentPos.mdist(bboxL);
			int distH = currentPos.mdist(bboxH);
			
			int z0, z1, zDir;
			if (distL < distH) {
				z0 = bboxL.z;
				z1 = bboxH.z + 1;
				zDir = 1;
			} else {
				z0 = bboxH.z;
				z1 = bboxL.z - 1;
				zDir = -1;
			}
			
			for (int z = z0; z != z1; z += zDir) {
//				System.out.println("  line " + z + ":");
				// find the extrema for this line
				if (!tgtModel.getLineBBox(y, z, bboxL, bboxH)) {
					System.out.println("  no cells on line <x," + y + "," + z + ">");
					continue;
				}
				System.out.println("  line <x," + y + "," + z + "> bbox: " + bboxL + " --> " + bboxH);
				distL = currentPos.mdist(bboxL);
				distH = currentPos.mdist(bboxH);

				int x0, x1, xDir;
				if (distL < distH) {
					x0 = bboxL.x;
					x1 = bboxH.x + 1;
					xDir = 1;
				} else {
					x0 = bboxH.x;
					x1 = bboxL.x - 1;
					xDir = -1;
				}

				for (int x = x0; x != x1; x += xDir) {
					if (tgtModel.get(x, y, z)) {
						moveTo(x, y+1, z, currentPos);
						trace.add(Trace.fillDown);
						System.out.println("    fill: <" + x + "," + y + "," + z + ">");
					}
				}
			}
		}

		// flip (off) before we go home
		trace.add(Trace.flip);
		moveTo(0, 0, 0, currentPos);
		
		trace.add(Trace.halt);
	}
	

	// This is the reverse of the add routine. We start at the top and work downwards.
	// Still single bot, void voxel at y-1.
	private void runDelete() {
		Vector3 currentPos = new Vector3(0, 0, 0);
		Vector3 bboxL = new Vector3(0, 0, 0);
		Vector3 bboxH = new Vector3(0, 0, 0);

		trace.add(Trace.flip);

		for (int y = srcModel.r - 1; y >= 0; y--) {
			System.out.println("Current pos: " + currentPos);
			if (!srcModel.getPlaneBBox(y, bboxL, bboxH)) {
				System.out.println("no cells on plane " + y);
				continue;
			}
			System.out.println("plane " + y + " bbox: " + bboxL + " --> " + bboxH);
			int distL = currentPos.mdist(bboxL);
			int distH = currentPos.mdist(bboxH);
			
			int z0, z1, zDir;
			if (distL < distH) {
				z0 = bboxL.z;
				z1 = bboxH.z + 1;
				zDir = 1;
			} else {
				z0 = bboxH.z;
				z1 = bboxL.z - 1;
				zDir = -1;
			}
			
			for (int z = z0; z != z1; z += zDir) {
//				System.out.println("  line " + z + ":");
				// find the extrema for this line
				if (!srcModel.getLineBBox(y, z, bboxL, bboxH)) {
					System.out.println("  no cells on line <x," + y + "," + z + ">");
					continue;
				}
				System.out.println("  line <x," + y + "," + z + "> bbox: " + bboxL + " --> " + bboxH);
				distL = currentPos.mdist(bboxL);
				distH = currentPos.mdist(bboxH);

				int x0, x1, xDir;
				if (distL < distH) {
					x0 = bboxL.x;
					x1 = bboxH.x + 1;
					xDir = 1;
				} else {
					x0 = bboxH.x;
					x1 = bboxL.x - 1;
					xDir = -1;
				}

				for (int x = x0; x != x1; x += xDir) {
					if (srcModel.get(x, y, z)) {
						moveTo(x, y+1, z, currentPos);
						trace.add(Trace.voidDown);
						System.out.println("    void: <" + x + "," + y + "," + z + ">");
					}
				}
			}
		}

		// flip (off) before we go home
		trace.add(Trace.flip);
		moveTo(0, 0, 0, currentPos);
		
		trace.add(Trace.halt);
	}
	
	
	private void runReplace() {
		Vector3 currentPos = new Vector3(0, 0, 0);
		Vector3 globalBBoxL = new Vector3(0, 0, 0);
		Vector3 globalBBoxH = new Vector3(0, 0, 0);
		if (!srcModel.deltaGlobalBBox(tgtModel, globalBBoxL, globalBBoxH)) {
			// um... nothing to do?
			System.out.println("models are identical!");
			trace.add(Trace.halt);
			return;
		}
		System.out.println("global bbox: " + globalBBoxL + " --> " + globalBBoxH);
		
		Vector3 bboxL = new Vector3(0, 0, 0);
		Vector3 bboxH = new Vector3(0, 0, 0);
		
		int xDir = +1;
		int zDir = +1;
		
		trace.add(Trace.flip);
		for (int y = globalBBoxL.y; y <= globalBBoxH.y; y++) {
			System.out.println("Current pos: " + currentPos);
			if (!srcModel.deltaPlaneBBox(tgtModel, y, bboxL, bboxH)) {
				System.out.println("no diff on plane " + y);
				continue;
			}
//			System.out.println("plane " + y + " bbox: " + bboxL + " --> " + bboxH);
//			System.out.println("xDir=" + xDir + "; zDir=" + zDir);
			
			moveTo( (xDir > 0) ? globalBBoxL.x - 1 : globalBBoxH.x + 1,
					y, 
					(zDir > 0) ? globalBBoxL.z : globalBBoxH.z, 
					currentPos);
			
			int zStart = (zDir > 0) ? globalBBoxL.z     : globalBBoxH.z;
			int zEnd   = (zDir > 0) ? globalBBoxH.z + 1 : globalBBoxL.z - 1;
			for (int z = zStart; z != zEnd; z += zDir) {
//				System.out.println("    z=" + z);
				
				boolean anyXMoves = false;
				int xStart = (xDir > 0) ? globalBBoxL.x - 1 : globalBBoxH.x + 1;
				int xEnd   = (xDir > 0) ? globalBBoxH.x + 2 : globalBBoxL.x - 2;
				for (int x = xStart; x != xEnd; x += xDir) {
					boolean eraseAhead;
					boolean fillBehind;
					if (xDir > 0) {
						eraseAhead = x < xEnd - 2;
						fillBehind = x > xStart + 1; 
					} else {
						eraseAhead = x > xEnd + 2;
						fillBehind = x < xStart - 1;
					}
//					System.out.println("      x=" + x + (eraseAhead ? " *" : " -") + (fillBehind ? " *" : " -"));
					boolean shouldClear = eraseAhead && srcModel.get(x + xDir, y, z);
					boolean shouldWrite = fillBehind && tgtModel.get(x - xDir, y, z);
					
					if (shouldClear || shouldWrite) {
						anyXMoves = true;
						moveTo(x, y, z, currentPos);
						if (shouldClear) {
//							System.out.println("        ++void");
							trace.add((xDir > 0) ? Trace.voidPosX : Trace.voidNegX);
						}
						if (shouldWrite) {
//							System.out.println("        ++fill");
							trace.add((xDir > 0) ? Trace.fillNegX : Trace.fillPosX);
						}
					}
				}
				if (anyXMoves) {
					moveTo(xEnd - xDir, y, z, currentPos);
					xDir = -xDir;
				}
			}
			zDir = -zDir;
		}

		// flip (off) before we go home
		trace.add(Trace.flip);
		moveTo(0, 0, 0, currentPos);
		
		trace.add(Trace.halt);
	}
	
	
	private void moveTo(int x1, int y1, int z1, Vector3 currentPos) {
		// TODO optimize, consolidate
		
//		System.out.println("        ++moveto <" + x1 + "," + y1 + "," + z1 + ">");
		
		// first move up
		if (currentPos.y < y1) {
			int dy = y1 - currentPos.y;
			while (dy != 0) {
				int dy0 = (dy > 15) ? 15 : ((dy < -15) ? -15 : dy);
				SMove move = new SMove(new Vector3(0, dy0, 0));
				trace.add(move);
				dy -= dy0;
			}
			currentPos.y = y1;
		}
		
		// next move in z-dir (front/back)
		if (currentPos.z != z1) {
			int dz = z1 - currentPos.z;
			while (dz != 0) {
				int dz0 = (dz > 15) ? 15 : ((dz < -15) ? -15 : dz);
				SMove move = new SMove(new Vector3(0, 0, dz0));
				trace.add(move);
				dz -= dz0;
			}
			currentPos.z = z1;
		}
		
		
		// next move in x-dir (left/right)
		if (currentPos.x != x1) {
			int dx = x1 - currentPos.x;
			while (dx != 0) {
				int dx0 = (dx > 15) ? 15 : ((dx < -15) ? -15 : dx);
				SMove move = new SMove(new Vector3(dx0, 0, 0));
				trace.add(move);
				dx -= dx0;
			}
			currentPos.x = x1;
		}
		
		// fnially, move down
		if (currentPos.y > y1) {
			int dy = y1 - currentPos.y;
			while (dy != 0) {
				int dy0 = (dy > 15) ? 15 : ((dy < -15) ? -15 : dy);
				SMove move = new SMove(new Vector3(0, dy0, 0));
				trace.add(move);
				dy -= dy0;
			}
			currentPos.y = y1;
		}
	}

	public void printTrace(PrintStream out) {
		out.println("#Commands: " + trace.size());
		out.println("Commands:");
		for (Trace op : trace) {
			out.println("    " + op);
		}
	}
}
