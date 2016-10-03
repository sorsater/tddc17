public class StateAndReward {

	// angle
	public static int ANGLE_REWARD = 10;
	public static int ANGLE_PRECISION = 6;
	public static double ANGLE_LIMIT = Math.PI;

	public static int ANGLE_TUNING_REWARD = 15;
	public static int ANGLE_TUNING_PRECISION = 6;
	public static double ANGLE_TUNING_LIMIT = 0.3; //ANGLE_LIMIT / 10;

	// velocity
	public static int VY_REWARD = 8;
	public static int VY_PRECISION = 6;
	public static double VY_LIMIT = 8;

	public static int VY_TUNING_REWARD = 12; // 6
	public static int VY_TUNING_PRECISION = 6;
	public static double VY_TUNING_LIMIT = 2; //VY_LIMIT / 5;

	// magic constants to tune the "perfect mode"
	// is in three levels
	public static double ANGLE_1 = 0.02;
	public static double ANGLE_2 = 0.05;
	public static double ANGLE_3 = 0.1;

	public static double VY_1 = 0.02;
	public static double VY_2 = 0.05;
	public static double VY_3 = 0.2;

	public static double VX_1 = 0.07;
	public static double VX_2 = 0.1;
	public static double VX_3 = 0.3;

	public static int REWARD_1 = 5000;
	public static int REWARD_2 = 2000;
	public static int REWARD_3 = 500;




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
		double angle_abs = Math.abs(angle);
		double vy_abs = Math.abs(vy);
		double vx_abs = Math.abs(vx);

		// angle
		// raw tuning
		state = (angle >= 0) ? "AH_" : "AV_";
		int angle_res = discretize(angle_abs, ANGLE_PRECISION, 0, ANGLE_LIMIT);
		state += angle_res;

		// fine tuning if low value
		if(angle_abs < ANGLE_TUNING_LIMIT){
			int angle_extra = discretize(angle_abs, ANGLE_TUNING_PRECISION, 0, ANGLE_TUNING_LIMIT);
			state += "-AF_" + angle_extra;
		}

		// vertical velocity
		// raw tuning
		state += (vy >= 0) ? "-YD_" : "-YU_";
		int vy_res = discretize2(vy_abs, VY_PRECISION, 0, VY_LIMIT);
		state += vy_res;

		// fine tuning if low value
		if(vy_abs < VY_TUNING_LIMIT){
			int vy_extra = discretize(vy_abs, VY_TUNING_PRECISION, 0, VY_TUNING_LIMIT);
			state += "-YF_" + vy_extra;
		}

		if(angle_abs < ANGLE_1 && vy_abs < VY_1 && vx_abs < VX_1){
			state += "GODLIKE";
		}
		else if(angle_abs < ANGLE_2 && vy_abs < VY_2 && vx_abs < VX_2){
			state += "godlike";
		}

		if(angle_abs < ANGLE_3 && vy_abs < VY_3 && vx_abs < VX_3){
			state += "insane";
		}

		return state;
	}

	public static double getRewardHover(double angle, double vx, double vy) {

		double reward = 0;
		double angle_abs = Math.abs(angle);
		double vy_abs = Math.abs(vy);
		double vx_abs = Math.abs(vx);

		// angle
		// raw tuning
		int angle_res = discretize(angle_abs, ANGLE_PRECISION, 0, ANGLE_LIMIT);
		reward += (ANGLE_PRECISION - angle_res) * ANGLE_REWARD;

		// fine tuning if low value
		if(angle_abs < ANGLE_TUNING_LIMIT){
			int angle_extra = discretize(angle_abs, ANGLE_TUNING_PRECISION, 0, ANGLE_TUNING_LIMIT);
			reward += (ANGLE_TUNING_PRECISION - angle_extra) * ANGLE_TUNING_REWARD;
		}

		// vertical velocity
		// raw tuning
		int vy_res = discretize2(vy_abs, VY_PRECISION, 0, VY_LIMIT);
		reward += (VY_PRECISION - vy_res) * VY_REWARD;

		// fine tuning if low value
		if(vy_abs < VY_TUNING_LIMIT){
			int vy_extra = discretize(vy_abs, VY_TUNING_PRECISION, 0, VY_TUNING_LIMIT);
			reward += (VY_TUNING_PRECISION - vy_extra) * VY_TUNING_REWARD;
		}

		// magic constants to give the rocket "perfect" states
		if(angle_abs < ANGLE_1 && vy_abs < VY_1 && vx_abs < VX_1){
			reward += 5000;
		}
		else if(angle_abs < ANGLE_2 && vy_abs < VY_2 && vx_abs < VX_2){
			reward += 2000;
		}
		else if(angle_abs < ANGLE_3 && vy_abs < VY_3 && vx_abs < VX_3){
			reward += 500;
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
