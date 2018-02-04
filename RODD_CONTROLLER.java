
public class RODD_CONTROLLER {

	
	// float?
	public int move(int speed)
	{	
		return 0;
	}
	
	public boolean object_between_tracks ()
	{
		
		return false;
	}
	
	public boolean obstacle_on_rails()
	{
		return false;
	}
	
	// Assuming array[0] is left and array[1] is right
	public float[][] read_infrared_cameras ()
	{
		float[][] test = new float[2][100];
		
		return test;
	}
	
	// Assuming array[0] is left and array[1] is right
	public float[][] read_rail_cameras ()
	{
		return new float[2][100];
	}
	
	public String[] transceiver_in ()
	{
		// The keys are:
		// 0 - DISTANCE
		// 1 - POWER
		// 2 - ANYTHING ELSE string to parse in case of other scenarios
		return new String[3];
		
	}
	
	public boolean transceiver_out (String data)
	{
		return true;		
	}
	
	public boolean power(int mode)
	{
		return true;
	}
	
	public boolean light(int light, int mode)
	{
		return true;
	}
	
	
}
