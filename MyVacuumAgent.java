package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

import java.util.Random;

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

  @Override public String toString() {
    String res;
    res = "Node: { \n";
    res += "  X: " + this.x + "\n";
    res += "  Y: " + this.y + "\n";
    res += "  P_X: " + this.parent_x + "\n";
    res += "  P_Y: " + this.parent_y + "\n";
    res += "  d: " + this.distance + "\n";
    res += "}\n";
    return res;
  }
}

class MyAgentState
{
	public int[][] world = new int[30][30];
	Node[][] nodeWorld = new Node[30][30];
	Queue<Node> qNodes = new LinkedList<Node>();

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

	public int goal_x = -1;
	public int goal_y = -1;

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
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" c ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();

	// Here you can define your variables!
	public int iterationCounter = 10;
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

	public void updateDirection(int direction){
		System.out.println("DIR: " + state.agent_direction);
		if(direction == 2){
			state.agent_direction = (state.agent_direction + 1) % 4;
		}
		else if(direction == 3){
			state.agent_direction -= 1;

			if(state.agent_direction < 0){
				state.agent_direction += 4;
			}
		}
		System.out.println("DIR: " + state.agent_direction);
	}

	public Action goTo(int x, int y){
		// to much east
		if(state.agent_x_position > x){
			if (state.agent_direction == state.WEST){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				updateDirection(state.ACTION_TURN_LEFT);
				state.agent_last_action = state.ACTION_TURN_LEFT;
				return LIUVacuumEnvironment.ACTION_TURN_LEFT;
			}
		}
		// to much south
		else if(state.agent_y_position > y){
			if (state.agent_direction == state.NORTH){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				updateDirection(state.ACTION_TURN_LEFT);
				state.agent_last_action = state.ACTION_TURN_LEFT;
				return LIUVacuumEnvironment.ACTION_TURN_LEFT;

			}
		}
		// to much west
		else if(state.agent_x_position < x){
			if (state.agent_direction == state.EAST){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				updateDirection(state.ACTION_TURN_LEFT);
				state.agent_last_action = state.ACTION_TURN_LEFT;
				return LIUVacuumEnvironment.ACTION_TURN_LEFT;

			}
		}
		// to much north
		//else if(state.agent_y_position < y){
    else{
			if (state.agent_direction == state.SOUTH){
				state.agent_last_action = state.ACTION_MOVE_FORWARD;
				return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
			}
			else {
				updateDirection(state.ACTION_TURN_LEFT);
				state.agent_last_action = state.ACTION_TURN_LEFT;
				return LIUVacuumEnvironment.ACTION_TURN_LEFT;

			}
		}
	}

  public Action goToImproved(){
    int H_X = state.agent_x_position;
    int H_Y = state.agent_y_position;

    int G_X = state.goal_x;
    int G_Y = state.goal_y;

    if(G_X == H_X && G_Y == H_Y){
      findUnexplored();
      G_X = state.goal_x;
      G_Y = state.goal_y;
    }

    System.out.println("H_X: " + state.agent_x_position + " H_Y: " + state.agent_y_position);
    System.out.println("G_X: " + G_X + " G_Y: " + G_Y);
    System.out.println("P_X: " + state.nodeWorld[G_X][G_Y].getParent_x());
    System.out.println("P_Y: " + state.nodeWorld[G_X][G_Y].getParent_y());


    int P_X = state.nodeWorld[G_X][G_Y].getParent_x();
    int P_Y = state.nodeWorld[G_X][G_Y].getParent_y();

    System.out.println(state.nodeWorld[G_X][G_Y].toString());

    int Prev_P_X = G_X;
    int Prev_P_Y = G_Y;

    while(true){
      // loopa tills vi har en nod precis bredvid. gå till den.

      System.out.println("Prev_P_X: " + Prev_P_X);
      System.out.println("Prev_P_Y: " + Prev_P_Y);

      if(P_X == H_X && P_Y == H_Y){
        return goTo(Prev_P_X,Prev_P_Y);
      }

      System.out.println(P_X + " JDJJ " + P_Y);
      System.out.println(state.nodeWorld[P_X][P_Y].toString());

      Prev_P_X = P_X;
      Prev_P_Y = P_Y;

      P_X = state.nodeWorld[Prev_P_X][Prev_P_Y].getParent_x();
      P_Y = state.nodeWorld[Prev_P_X][Prev_P_Y].getParent_y();
    }
  }

	public Boolean BFSNode(int old_x,int old_y,int x,int y){
		if(x < 1 || x >= 29 || y < 1 || y >= 29){
			return false;
		}

    int c = state.nodeWorld[old_x][old_y].getDistance();
    System.out.println("COST IS: " + c);

		if(state.nodeWorld[x][y].getDistance() == -1){
			// TODO overkilla med direction som kostnad
			if(state.world[x][y] != state.WALL){

				state.nodeWorld[x][y].setDistance(c + 1);
				state.nodeWorld[x][y].setParent(old_x,old_y);

        if(state.world[x][y] == state.UNKNOWN){
          // go to this little sucker
          System.out.println("come here: " + x + " : " + y);
          return true;
        }
				state.qNodes.add(state.nodeWorld[x][y]);
			}
		}
    return false;
	}

	public void findUnexplored(){

		for(int i = 0; i < 30; i++){
			for(int j = 0; j < 30; j++){
				state.nodeWorld[i][j] = new Node(i,j);
			}
		}

    state.qNodes.clear();

		state.nodeWorld[state.agent_x_position][state.agent_y_position].setDistance(0);
		state.qNodes.add(state.nodeWorld[state.agent_x_position][state.agent_y_position]);

    int x;
    int y;
		while(state.qNodes.size() > 0){
			Node current = state.qNodes.poll();

			x = current.getX();
			y = current.getY();

			if(BFSNode(x,y,x+1,y)){
        state.goal_x = x + 1;
        state.goal_y = y;
        break;
      }
			if(BFSNode(x,y,x-1,y)){
        state.goal_x = x - 1;
        state.goal_y = y;
        break;
      }
      if(BFSNode(x,y,x,y+1)){
        state.goal_x = x;
        state.goal_y = y + 1;
        break;
      }
      if(BFSNode(x,y,x,y-1)){
        state.goal_x = x;
        state.goal_y = y - 1;
        break;
      }
		}
    return;
	}

	public Action searchAndSuck(Boolean bump, Boolean dirt, Boolean home){

		if(state.goal_x == -1 && state.goal_y == -1){
			findUnexplored();
		}
    if(dirt){
      state.agent_last_action = state.ACTION_SUCK;
      return LIUVacuumEnvironment.ACTION_SUCK;
    }
		if(bump){
			findUnexplored();

      for(int i = 0 ; i < 7; i++){
  			for(int j = 0 ; j < 7; j++){
  				System.out.print(" " + state.nodeWorld[j][i].distance + " ");
  			}
  			System.out.println("");
  		}
		}

    Action next = goToImproved();
    System.out.println("NEXT: " + next);
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

  	// This example agent program will update the internal agent state while only moving forward.
  	// START HERE - code below should be modified!


    //iterationCounter--;

    if (iterationCounter==0)
    	return NoOpAction.NO_OP;

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

	  // Next action selection based on the percept value
		System.out.println("==========================================================");
		System.out.println("x=" + state.agent_x_position);
		System.out.println("y=" + state.agent_y_position);
		System.out.println("dir=" + state.agent_direction);

		state.printWorldDebug();

		return searchAndSuck(bump, dirt, home);
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
