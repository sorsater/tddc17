package tddc17;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

// used to get an working queue
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

import java.util.Random;

// class for keeping track of all the nodes.
// members are:
// 		* distance - the cost for that node
// 		* parent - coordinates for the parents
// 		* xy	- coordiantes for the node
class Node {
	public int distance;
	public int parent_x;
	public int parent_y;
	public int x;
	public int y;

	public Node(int x, int y){
		this.distance = -1;
		this.parent_x = -1;
		this.parent_y = -1;
		this.x = x;
		this.y = y;
	}

	public void setDistance(int distance){
		this.distance = distance;
	}

	public void setParent(int x,int y){
		this.parent_x = x;
		this.parent_y = y;
	}

	public int getDistance(){
		return this.distance;
	}

	public int getParent_x(){
		return this.parent_x;
	}

	public int getParent_y(){
		return this.parent_y;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}
}

class MyAgentState
{
	public int[][] world = new int[30][30];

	// the grid for storing the BFS-grid
	public Node[][] nodeWorld = new Node[30][30];
	// queue for BFS
	public Queue<Node> qNodes = new LinkedList<Node>();

	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;//    return result.toString();

	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;

	// mapdone is set when no more unknown positions
	public Boolean mapDone = false;
	// the goal coordinates
	public int goal_x = -1;
	public int goal_y = -1;

	// the bfs search looks this far in the world
	// is updated when discovering a wall
	public int wall_x = 29;
	public int wall_y = 29;
 
 	// used for keeping track of bumps into the wall
	public int maxX = 0;
	public int maxY = 0;
	public int maxX_counter = 0;
	public int maxY_counter = 0;
	// number of bumps to update the wall
	public final int number_of_bumps = 4;
	
	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
		{
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
		}
	}

	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}

	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" . ");
				if (world[j][i]==WALL)
					System.out.print(" | ");
				if (world[j][i]==CLEAR)
					System.out.print("   ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" ! ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();

	// Here you can define your variables!
	public int iterationCounter = 15*15*2;
	public MyAgentState state = new MyAgentState();

	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if(action==0) {
			state.agent_direction = ((state.agent_direction-1) % 4);
			if (state.agent_direction<0)
				state.agent_direction +=4;
					System.out.println("DIR: " + state.agent_direction);

			state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
			System.out.println("DIR: " + state.agent_direction);

			state.agent_last_action = state.ACTION_TURN_RIGHT;
			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		}
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}

	// keeps track of the current direction
	public void updateDirection(int direction){
		if(direction == state.ACTION_TURN_RIGHT){
			state.agent_direction = (state.agent_direction + 1) % 4;
		}
		else if(direction == state.ACTION_TURN_LEFT){
			state.agent_direction -= 1;
			if(state.agent_direction < 0){
				state.agent_direction += 4;
			}
		}
	}

	// goes to the adjacent position
	public Action goTo(int x, int y){
		// to much east
		if(state.agent_x_position > x){
			if (state.agent_direction == state.WEST){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				if(state.agent_direction == state.SOUTH){
					updateDirection(state.ACTION_TURN_RIGHT);
					state.agent_last_action = state.ACTION_TURN_RIGHT;
					return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
				}
				else {
					updateDirection(state.ACTION_TURN_LEFT);
					state.agent_last_action = state.ACTION_TURN_LEFT;
					return LIUVacuumEnvironment.ACTION_TURN_LEFT;
				}
			}
		}
		// to much west
		else if(state.agent_x_position < x){
			if (state.agent_direction == state.EAST){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				if(state.agent_direction == state.NORTH){
					updateDirection(state.ACTION_TURN_RIGHT);
					state.agent_last_action = state.ACTION_TURN_RIGHT;
					return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
				}
				else {
					updateDirection(state.ACTION_TURN_LEFT);
					state.agent_last_action = state.ACTION_TURN_LEFT;
					return LIUVacuumEnvironment.ACTION_TURN_LEFT;
				}
			}
		}
		// to much south
		else if(state.agent_y_position > y){
			if (state.agent_direction == state.NORTH){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				if(state.agent_direction == state.WEST){
					updateDirection(state.ACTION_TURN_RIGHT);
					state.agent_last_action = state.ACTION_TURN_RIGHT;
					return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
				}
				else {
					updateDirection(state.ACTION_TURN_LEFT);
					state.agent_last_action = state.ACTION_TURN_LEFT;
					return LIUVacuumEnvironment.ACTION_TURN_LEFT;
				}
			}
		}
		// to much north
		else{
			if (state.agent_direction == state.SOUTH){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				if(state.agent_direction == state.EAST){
					updateDirection(state.ACTION_TURN_RIGHT);
					state.agent_last_action = state.ACTION_TURN_RIGHT;
					return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
				}
				else {
					updateDirection(state.ACTION_TURN_LEFT);
					state.agent_last_action = state.ACTION_TURN_LEFT;
					return LIUVacuumEnvironment.ACTION_TURN_LEFT;
				}
			}
		}
	}

	// backtracks the BFS and creates an path of nodes to the goal
	public Action goToImproved(){
		int H_X = state.agent_x_position;
		int H_Y = state.agent_y_position;

		int G_X = state.goal_x;
		int G_Y = state.goal_y;

		// if in desired position
		if(G_X == H_X && G_Y == H_Y){
			// if no more unknown (and we are at 1,1)
			if(state.mapDone){
				return NoOpAction.NO_OP;
			}
			findUnexplored();
			G_X = state.goal_x;
			G_Y = state.goal_y;
		}

		int P_X = state.nodeWorld[G_X][G_Y].getParent_x();
		int P_Y = state.nodeWorld[G_X][G_Y].getParent_y();

		int Prev_P_X = G_X;
		int Prev_P_Y = G_Y;

		while(true){
			// loop until parent is the current node
			if(P_X == H_X && P_Y == H_Y){
				return goTo(Prev_P_X,Prev_P_Y);
			}

			// update parents
			Prev_P_X = P_X;
			Prev_P_Y = P_Y;

			P_X = state.nodeWorld[Prev_P_X][Prev_P_Y].getParent_x();
			P_Y = state.nodeWorld[Prev_P_X][Prev_P_Y].getParent_y();
		}
	}
	
	// expands the BFS
	public Boolean BFSNode(int old_x,int old_y,int x,int y){
		// the limits for the world
		if(x < 1 || x > state.wall_x || y < 1 || y > state.wall_y){
			return false;
		}

		int c = state.nodeWorld[old_x][old_y].getDistance();

		// unexplored
		if(state.nodeWorld[x][y].getDistance() == -1){
			// ignores walls
			if(state.world[x][y] != state.WALL){
				state.nodeWorld[x][y].setDistance(c + 1);
				state.nodeWorld[x][y].setParent(old_x,old_y);
				// reaches goalnode
				if(state.world[x][y] == state.UNKNOWN){
					return true;
				}
				// add node the the queue
				state.qNodes.add(state.nodeWorld[x][y]);
			}
		}
		return false;
	}

	// creates the BFS grid
	public void findUnexplored(){
		// reset all
		for(int i = 0; i < 30; i++){
			for(int j = 0; j < 30; j++){
				state.nodeWorld[i][j] = new Node(i,j);
			}
		}
		state.qNodes.clear();

		// add root
		state.nodeWorld[state.agent_x_position][state.agent_y_position].setDistance(0);
		state.qNodes.add(state.nodeWorld[state.agent_x_position][state.agent_y_position]);

		int x;
		int y;
		while(state.qNodes.size() > 0){
			Node current = state.qNodes.poll();

			x = current.getX();
			y = current.getY();

			// prioritize to go in the same direction as we stand because rotation costs 
			// ex: (1,0),(0,1),(0,-1),(-1,0) order: right, down, up, left

			// default 0 everywhere
			int[][] order = new int[4][2];
			if (state.agent_direction == state.EAST){
				order[0][0] = 1;
				order[3][0] = -1;
			}
			else if (state.agent_direction == state.WEST){
				order[0][0] = -1;
				order[3][0] = 1;
			}
			else if (state.agent_direction == state.NORTH){
				order[0][1] = -1;
				order[3][1] = 1;
			}
			else if (state.agent_direction == state.SOUTH){
				order[0][1] = 1;
				order[3][1] = -1;
			}

			// ex if standing east. up/down have the same cost. prioritize to search down and right
			if (state.agent_direction == state.EAST || state.agent_direction == state.WEST){
				order[1][1] = 1;
				order[2][1] = -1;
			}
			else if (state.agent_direction == state.NORTH || state.agent_direction == state.SOUTH){
				order[1][0] = 1;
				order[2][0] = -1;
			}

			// call BFS with priority order
			for (int i = 0 ; i < 4; i++){
				if (BFSNode(x,y,x + order[i][0],y + order[i][1])){
					state.goal_x = x + order[i][0];
					state.goal_y = y + order[i][1];
					return;
				}
			}
		}
		// if no return with updated positions, we are done. go home
		state.goal_x = 1;
		state.goal_y = 1;
		state.mapDone = true;
		return;
	}

	// "main". returns action from the BFS and updates world if dirt or bump
	public Action searchAndSuck(Boolean bump, Boolean dirt){
		// at startup, no goal set
		if(state.goal_x == -1 && state.goal_y == -1){
			findUnexplored();
		}
		if(dirt){
			state.agent_last_action = state.ACTION_SUCK;
			return LIUVacuumEnvironment.ACTION_SUCK;
		}
		if(bump){
			// checks the walls to set limit for the search
			if(state.agent_direction == state.EAST){
				if(state.agent_x_position > state.maxX){
					state.maxX = state.agent_x_position;
					state.maxX_counter = 0;
				}
				if(state.agent_x_position == state.maxX){
					state.maxX_counter++;
					if(state.maxX_counter == state.number_of_bumps){
						state.wall_x = state.agent_x_position;
					}
				}
			}
			else if(state.agent_direction == state.SOUTH){
				if(state.agent_y_position > state.maxY){
					state.maxY = state.agent_y_position;
					state.maxY_counter = 0;
				}
				if(state.agent_y_position == state.maxY){
					state.maxY_counter++;
					if(state.maxY_counter == state.number_of_bumps){
						state.wall_y = state.agent_y_position;
					}	
				}
			}
			// update BFS to compensate the path for the walls
			findUnexplored();
		}

		Action next = goToImproved();
		return next;
	}

	@Override
	public Action execute(Percept percept) {

		// DO NOT REMOVE this if condition!!!
		if (initnialRandomActions>0) {
			return moveToRandomStartPosition((DynamicPercept) percept);
		}
		else if (initnialRandomActions==0) {
			// process percept for the last step of the initial random actions
			initnialRandomActions--;
			state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
			return LIUVacuumEnvironment.ACTION_SUCK;
		}

		//iterationCounter--;
		if (iterationCounter==0){
			return NoOpAction.NO_OP;
		}

		DynamicPercept p = (DynamicPercept) percept;
		Boolean bump = (Boolean)p.getAttribute("bump");
		Boolean dirt = (Boolean)p.getAttribute("dirt");
		Boolean home = (Boolean)p.getAttribute("home");
		System.out.println(p);

		// State update based on the percept value and the last action
		state.updatePosition((DynamicPercept)percept);

		if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
		}
		if (dirt){
			state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
		}
		else{
			state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
		}

		state.printWorldDebug();

		// the "main method"
		return searchAndSuck(bump, dirt);
	}
}

public class MyVacuumAgent extends AbstractAgent {
	public MyVacuumAgent() {
		super(new MyAgentProgram());
	}
}
