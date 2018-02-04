import java.time.*;
import java.nio.file.Paths;
import java.nio.file.*;


import org.python.google.common.io.Files;
import org.python.util.PythonInterpreter;

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
	public static int stableSpeed = 25; // 25 m/s ~ 80 km/h
	public static int iteration = 0;
	// This is a placeholder class
	public static RODD_CONTROLLER rodd;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		rodd = new RODD_CONTROLLER();
		
		Process_Rodd(3000);
		

	}
	
	// This function can be renamed, this is only a placeholder
	public static void Process_Rodd(int DIST_FROM_TRAIN)
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
				iteration += 1;
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
	public static void Alert(String message)
	{
		boolean response = false;
		// Continuously try to relay the message until it is relayed
		while(!response)
		{
			response = rodd.transceiver_out(message);
		}
	}
	
	// returns True if there's a concerning infrared shape
	public static boolean Infrared_Obstacle()
	{
		int[][] img  = rodd.read_infrared_cameras ();
		
		if(false)
		{
			Alert("Warning: Animal detected");
		}
		
		return false;
	}
	
	// returns True if there's a concerning UV shape
	public static boolean UV_Obstacle()
	{
		int[][] img = rodd.read_rail_cameras();
		
		//TODO
		// call python function
		
		if(false)
		{
			Alert("Warning: Cracks detected");
		}
		
		return false;
	}
	
	// Checks if device is too close to the train
	public static int dist_thresh = 15;
	public static boolean Check_Distance(int dist, int lastDist, int DIST_FROM_TRAIN)
	{
		if(dist < DIST_FROM_TRAIN)
		{
			
			System.out.println("Dist to train: " + dist);
			// This is the difference in speed of the train and the locomotive
			// (d1-d2)/t = dv
			float trainSpeed = (dist - lastDist)/(timeThresh/1000000000);
			
			// The locomotive should be going at the same speed of the train usually
			// So stable speed is speed + difference in speed
			// v = v + dv
			stableSpeed = speed + Math.round(trainSpeed);
			
			// To reach the distance thresh in 200ms
			// v = v + dv + (dD/t)
			// We overestimate by dist_thresh
			int accel = Math.round((dist_thresh + DIST_FROM_TRAIN - dist)/(timeThresh/1000000000)) + stableSpeed;			
			
			// Can't go faster than the max speed
			if(accel > maxSpeed)
			{
				accel = maxSpeed;
			}
			System.out.println("old speed :" + speed);
			speed = rodd.move(accel);
			System.out.println("New speed :" + speed);
		}
		// Make sure we're going at the stable speed
		else
		{
			speed = rodd.move(stableSpeed);
		}
		
		// If the speed is much less than the desired speed
		// There might be an obstacle on the rails
		if( stableSpeed <= 1 || (float)speed/stableSpeed < 0.1)
		{
			Alert("WARNING: Trouble speeding up. There might be an obstacle on the rails");
			return false;
		}
		
		return true;
		
	}
	
}
