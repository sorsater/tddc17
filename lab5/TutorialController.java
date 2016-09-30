public class TutorialController extends Controller {

	public SpringObject object;

	ComposedSpringObject cso;

	/* These are the agents senses (inputs) */
	DoubleFeature x; /* Positions */
	DoubleFeature y;
	DoubleFeature vx; /* Velocities */
	DoubleFeature vy;
	DoubleFeature angle; /* Angle */

	/* Example:
	 * x.getValue() returns the vertical position of the rocket 
	 */

	/* These are the agents actuators (outputs)*/
	RocketEngine leftRocket;
	RocketEngine middleRocket;
	RocketEngine rightRocket;

	/* Example:
	 * leftRocket.setBursting(true) turns on the left rocket 
	 */
	
	public void init() {
		cso = (ComposedSpringObject) object;
		x = (DoubleFeature) cso.getObjectById("x");
		y = (DoubleFeature) cso.getObjectById("y");
		vx = (DoubleFeature) cso.getObjectById("vx");
		vy = (DoubleFeature) cso.getObjectById("vy");
		angle = (DoubleFeature) cso.getObjectById("angle");

		leftRocket = (RocketEngine) cso.getObjectById("rocket_engine_left");
		rightRocket = (RocketEngine) cso.getObjectById("rocket_engine_right");
		middleRocket = (RocketEngine) cso.getObjectById("rocket_engine_middle");
	}

	public void tick(int currentTime) {

		System.out.println("X: " + x.getValue());
		System.out.println("Y: " + y.getValue());
		System.out.println("VX: " + vx.getValue());
		System.out.println("VY: " + vy.getValue());
		System.out.println("Angle: " + angle.getValue());
	//	middleRocket.setBursting(false);
	//	rightRocket.setBursting(false);
	//	leftRocket.setBursting(false);

/*
		if(angle.getValue() < 0){
			leftRocket.setBursting(true);
			rightRocket.setBursting(false);
		}
		else {
			leftRocket.setBursting(false);
			rightRocket.setBursting(true);
		}

		if(y.getValue() < -1000){
			leftRocket.setBursting(false);
			rightRocket.setBursting(false);	
		}
		if(y.getValue() > 0){
			middleRocket.setBursting(true);
		}

		if(x.getValue() > 1000){
			rightRocket.setBursting(true);
		}
*/

		double val = 0.5;
		

		if(vy.getValue() > 0){
			middleRocket.setBursting(true);
		}

		if(vx.getValue() < -val){
			rightRocket.setBursting(false);
			leftRocket.setBursting(true);
		}
		else if(vx.getValue() > val){
			rightRocket.setBursting(true);
			leftRocket.setBursting(false);
		}
		
	}

}
