public class StateAndReward {

	
	/* State discretization function for the angle controller */
	public static String getStateAngle(double angle, double vx, double vy) {

		String state = "OneStateToRuleThemAll";

		int res = discretize(angle,31,-Math.PI,Math.PI);

		if(res == 15){
			state = "Up";
		}
		else if(res < 15 && res > 8){
			state = "Left_Light";
		}
		else if(res < 9){
			state = "Left_Strong";
		}
		else if(res > 15 && res < 24){
			state = "Right_Light";
		}
		else if(res > 23){
			state = "Right_Strong";
		}

		return state;
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		double reward = 0;

		int res = discretize(angle,31,-Math.PI,Math.PI);

		if(res == 15){
			reward = 100;
		}
		else if(res < 15 && res > 8){
			reward = 45;
		}
		else if(res < 9){
			reward = 20;
		}
		else if(res > 15 && res < 24){
			reward = 45;
		}
		else if(res > 23){
			reward = 20;
		}

		return reward;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {

		String state = "OneStateToRuleThemAll";

		int angle_res = discretize(angle,31,-Math.PI,Math.PI);

		if(angle_res == 15){
			state = "Up";
		}
		else if(angle_res < 15 && angle_res > 11){
			state = "Left_Strong";
		}
		else if(angle_res < 12 && angle_res > 7){
			state = "Left_Light";
		}
		else if(angle_res < 8){
			state = "Down";
		}
		else if(angle_res > 15 && angle_res < 19){
			state = "Right_Strong";
		}
		else if(angle_res > 18 && angle_res < 23){
			state = "Right_Light";
		}
		else if(angle_res > 23){
			state = "Down";
		}

		//TODO discretize2 med vy + vx
		if(vy < 0.01 && vy > -0.01){
			state += "-Godlike";
		}
		else if(vy < 0.1 &&  vy > -0.1){
			state += "-Master";
		}
		else if(vy < 0.3 &&  vy > -0.3){
			state += "-Good";
		}
		else if(vy < 0.6 && vy > -0.6){
			state += "-OK";
		}
		else{
			state += "-Nothing";
		}

		return state;
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {

		double reward = 0;

		int angle_res = discretize(angle,31,-Math.PI,Math.PI);

		if(angle_res == 15){
			reward = 100;
		}
		else if(angle_res < 15 && angle_res > 11){
			reward = 45;
		}
		else if(angle_res < 12 && angle_res > 7){
			reward = 20;
		}
		else if(angle_res < 8){
			reward = 0;
		}
		else if(angle_res > 15 && angle_res < 19){
			reward = 45;
		}
		else if(angle_res > 18 && angle_res < 23){
			reward = 20;
		}
		else if(angle_res > 23){
			reward = 0;
		}

		//TODO discretize2 med vy + vx
	//	int middle = 15;
	//	int vy_res = discretize2(vy,middle * 2 + 1, -2,2);
		if(vy < 0.01 && vy > -0.01){
			reward += 200;
		}
		else if(vy < 0.1 &&  vy > -0.1){
			reward += 100;
		}
		else if(vy < 0.3 &&  vy > -0.3){
			reward += 50;
		}
		else if(vy < 0.6 && vy > -0.6){
			reward += 10;
		}
		else{
			reward += 0;
		}

		return reward;
	}

	// ///////////////////////////////////////////////////////////
	// discretize() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 1 and nrValues-2 is returned.
	//
	// Use discretize2() if you want a discretization method that does
	// not handle values lower than min and higher than max.
	// ///////////////////////////////////////////////////////////
	public static int discretize(double value, int nrValues, double min, double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	// ///////////////////////////////////////////////////////////
	// discretize2() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 0 and nrValues-1 is returned.
	// ///////////////////////////////////////////////////////////
	public static int discretize2(double value, int nrValues, double min, double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}

}
