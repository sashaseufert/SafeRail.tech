import java.time.*;
import java.io.*;
import org.*;
// This class can be renamed if needed
public class Rodd {
	
	// 200ms in nanometers
	public static float timeThresh = 200000000; 
	public static float lastTime;
	public static int speed = 100; // placeholder
	public static int maxSpeed = 100;
	public static boolean ON = true;
	public static int stableSpeed = 50;
	// This is a placeholder class
	RODD_CONTROLLER rodd;
	
	
		public static void main(String[] args) {
		// REPLACE WITH WHOLE PATH
		String command = "detect_uv.py";

		try
		{
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s = "";
		
        BufferedReader stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));

           BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(p.getErrorStream()));

           // read the output from the command
           System.out.println("Here is the standard output of the command:\n");
           while ((s = stdInput.readLine()) != null) {
               System.out.println(s);
           }
           
           // read any errors from the attempted command
           System.out.println("Here is the standard error of the command (if any):\n");
           while ((s = stdError.readLine()) != null) {
               System.out.println(s);
           }
           
		}
		catch(IOException ex) 
		{
			System.out.println("IO problem");
			System.out.println(ex);
		}
	}
	
	// This function can be renamed, this is only a placeholder
	public void Process_Rodd(int DIST_FROM_TRAIN)
	{
		// TODO: replace with actual name
		rodd = new RODD_CONTROLLER();
		lastTime = System.nanoTime() + timeThresh;
		int lastTrainDist = DIST_FROM_TRAIN;
		
		while(ON)
		{
			// Execute code every 200ms
			if(System.nanoTime() > lastTime + timeThresh)
			{
				lastTime = System.nanoTime();
				
				if(rodd.object_between_tracks())
				{
					Alert("WARNING: There may be an object between the tracks");
				}
				else if(rodd.obstacle_on_rails())
				{
					Alert("WARNING: There may be an obstacle on the rails");
				}
				else if(Infrared_Obstacle())
				{
					Alert("WARNING: There may be an animal on the tracks");
				}
				else if(UV_Obstacle())
				{
					Alert("WARNING: The rails may have metal fractures");
				}
				
				// Checking for messages
				String[] messages = rodd.transceiver_in();
				
				// Expecting
				// 0 - Distance
				// 1 - POWER
				// 2 - Any other information
				
				// 0th array entry should be the current distance from the train
				// TODO: raise flag if messages[0] is null (IT SHOULD NEVER BE NULL)
				if(messages[0] != null)
				{
					if(Check_Distance(Integer.parseInt(messages[0]), lastTrainDist, DIST_FROM_TRAIN))
					{
						lastTrainDist = Integer.parseInt(messages[0]);
					}
				}
				// Power
				if(messages[1] != null)
				{
					int mode = Integer.parseInt(messages[1]);					
					boolean power = rodd.power(mode);
					ON = power;
				}
				// Anything else
				if(messages[2] != null)
				{
					// TODO: parse the message for additional information
				}
								
			}
			
		}
		
	}
	
	
	// Sends an alert
	public void Alert(String message)
	{
		boolean response = false;
		// Continuously try to relay the message until it is relayed
		while(!response)
		{
			response = rodd.transceiver_out(message);
		}
	}
	
	// returns True if there's a concerning infrared shape
	public boolean Infrared_Obstacle()
	{
		//TODO
		return false;
	}
	
	// returns True if there's a concerning UV shape
	public boolean UV_Obstacle()
	{
		//TODO
		return false;
	}
	
	// Checks if device is too close to the train
	// TODO: check the math
	public boolean Check_Distance(int dist, int lastDist, int DIST_FROM_TRAIN)
	{
		if(dist < DIST_FROM_TRAIN)
		{
			// This is the difference in speed of the train and the locomotive
			// (d1-d2)/t = dv
			float trainSpeed = (dist - lastDist)/(timeThresh/1000000000);
			
			// The locomotive should be going at the same speed of the train usually
			// So stable speed is speed + difference in speed
			// v = v + dv
			stableSpeed = speed + Math.round(trainSpeed);
			
			// To reach the distance thresh in 200ms
			// v = v + dv + (dD/t)
			int accel = Math.round((DIST_FROM_TRAIN - dist)/(timeThresh/1000000000)) + stableSpeed;			
			
			// Can't go faster than the max speed
			if(accel > maxSpeed)
			{
				accel = maxSpeed;
			}
			
			speed = rodd.move(accel);
			
		}
		// Make sure we're going at the stable speed
		else
		{
			speed = rodd.move(stableSpeed);
		}
		
		// If the speed is much less than the desired speed
		// There might be an obstacle on the rails
		if(speed/stableSpeed < 0.1)
		{
			Alert("WARNING: There might be an obstacle on the rails");
			return false;
		}
		
		return true;
		
	}
	
}
