public class StateAndReward {

	public static int ANGLE_RESOLUTION = 6;
	public static int ANGLE_REWARD_FACTOR = 7;
	public static int ANGLE_MIN_VALUE = 0;
	public static double ANGLE_MAX_VALUE = Math.PI;

	public static int VY_RESOLUTION = 6;
	public static int VY_REWARD_FACTOR = 10;
	public static int VY_MIN_VALUE = 0;
	public static int VY_MAX_VALUE = 5;

	public static int VX_RESOLUTION = 3;
	public static int VX_REWARD_FACTOR = 3; //2
	public static int VX_MIN_VALUE = 0;
	public static int VX_MAX_VALUE = 1; //2

	// FINE TUNING

	public static double ANGLE_TUNING_LIMIT = 0.4;
	public static int ANGLE_TUNING_REWARD_FACTOR = 5;

	public static double VY_TUNING_LIMIT = 0.8;
	public static int VY_TUNING_REWARD_FACTOR = 5;

	// TODO 1 state för x
	// skriv om kod för precision så den är fin.

	public static String getStateAngle(double angle, double vx, double vy) {

		String state = "";

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

	public static String getStateHover(double angle, double vx, double vy) {

		String state = "";

		double abs_angle = Math.abs(angle);
		double abs_vy = Math.abs(vy);

		state = (angle >= 0) ? "R_" : "L_";
		int angle_res = discretize(abs_angle, ANGLE_RESOLUTION	, ANGLE_MIN_VALUE, ANGLE_MAX_VALUE);
		state += angle_res;

		state += (vy >= 0) ? "-D_" : "-U_";
		int vy_res = discretize2(abs_vy, VY_RESOLUTION, VY_MIN_VALUE, VY_MAX_VALUE);
		state += vy_res;

		if(abs_angle < ANGLE_TUNING_LIMIT){
			int angle_extra = discretize(abs_angle, 6, 0, ANGLE_TUNING_LIMIT);
			state += "-AP_" + angle_extra;
		}

		if(abs_vy < VY_TUNING_LIMIT){
			int vy_extra = discretize(abs_vy, 6, 0, VY_TUNING_LIMIT);
			state += "-YP_" + vy_extra;
		}

//		state += (vx >= 0) ? "-X_" : "-Z_";
//		int vx_res = discretize2(Math.abs(vx), VX_PRECISION, VX_MIN_VALUE, VX_MAX_VALUE);
//		state += vx_res;

// 178
		return state;
	}

	public static double getRewardHover(double angle, double vx, double vy) {

		double reward = 0;

		double abs_angle = Math.abs(angle);
		double abs_vy = Math.abs(vy);

		int angle_res = discretize(abs_angle, ANGLE_RESOLUTION, ANGLE_MIN_VALUE, ANGLE_MAX_VALUE);
		reward += (ANGLE_RESOLUTION - angle_res) * ANGLE_REWARD_FACTOR;

		int vy_res = discretize2(abs_vy, VY_RESOLUTION, VY_MIN_VALUE, VY_MAX_VALUE);
		reward += (VY_RESOLUTION - vy_res) * VY_REWARD_FACTOR;

//		int vx_res = discretize2(Math.abs(vx), VX_PRECISION, VX_MIN_VALUE, VX_MAX_VALUE);
//		reward += (VX_PRECISION - vx_res) * VX_REWARD_FACTOR;

		if(abs_angle < ANGLE_TUNING_LIMIT){
			int angle_extra = discretize(abs_angle, 6, 0, ANGLE_TUNING_LIMIT);
			reward += (6 - angle_extra) * ANGLE_TUNING_REWARD_FACTOR;
		}

		if(abs_vy < VY_TUNING_LIMIT){
			int vy_extra = discretize(abs_vy, 6, 0, VY_TUNING_LIMIT);
			reward += (6 - vy_extra) * VY_TUNING_REWARD_FACTOR;
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
