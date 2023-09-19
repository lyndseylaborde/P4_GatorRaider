package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int update(Game game,long timeDue)
	{
		int direction;
		Attacker user = game.getAttacker();
		List<Node> powerPills = game.getPowerPillList();
		List<Node> regularPills = game.getPillList();
		List<Defender> defender  = game.getDefenders();
		Node closestPill = user.getTargetNode(regularPills, true);


		// first: if ghosts are close by, eat if vulnerable

		for (Defender d : game.getDefenders()) { //check each defender
			if (user.getLocation().getPathDistance(d.getLocation()) <= 5 && user.getLocation().getPathDistance(d.getLocation()) >= 0) { //if it's nearby, check if it can be eaten or if you should run
				if (!d.isVulnerable()) {
					direction = user.getNextDir(d.getLocation(), false);
					return direction;
				}
				else {
					direction = user.getNextDir(d.getLocation(), true);
					return direction;
				}

			}
			else if (user.getLocation().getPathDistance(d.getLocation()) <= 50 && d.isVulnerable()) { //if it can be eaten but is not nearby, go after it
				direction = user.getNextDir(d.getLocation(), true);
				return direction;
			}
		}

		// second: check if there are power pills / if yes -> eat => idea from Andrew Penton (TA)
		if (powerPills.size() > 0) {
			Node closestPowerPill = user.getTargetNode(powerPills, true); //location of closest power pill
			Node closestDefender =  user.getTargetActor(defender, true).getLocation(); //location of closest defender
			if (user.getLocation().getPathDistance(closestDefender) <= 6) { //wait until defender is close by => idea from Marco (TA)
				direction = user.getNextDir(closestPowerPill, true); //get power pill
				return direction;
			}
			else {
				if (user.getLocation().getPathDistance(closestPowerPill) <= 6) { //wait by power pill
					direction = user.getReverse();
					return direction;
				}
				else {
					direction = user.getNextDir(closestPowerPill, true); //get power pill
					return direction;
				}
			}
		}

		// third: eat small pills
		else{
			direction = user.getNextDir(closestPill, true);
			return direction;
		}
	}
}