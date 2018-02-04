import java.nio.file.Paths;
import java.nio.file.*;
import java.io.*;
import org.python.google.common.io.Files;


public class RODD_CONTROLLER {
	
	int index = 0;
	int max_iter = 100;
	int train_dist = 3000;
	float timeThresh = 0.2f;
	// Introduces some randomness in the returned speed
	public int move(int speed)
	{	
		double undetected_obstacle = Math.random();
		
		// There is a small chance that an obstacle will be hit
		if(undetected_obstacle < 0.05)
		{
			return speed/20;
		}
		
		double sign = Math.random();
		
		int newspeed;
		
		if(sign > 0.5)
		{
			newspeed = speed - (int)Math.round((Math.random()*5));
		}
		else
		{
			newspeed = speed + (int)Math.round((Math.random()*5));
		}
		
		train_dist = train_dist + Math.round((newspeed - speed)*timeThresh);
		
		return newspeed;
	}
	
	public boolean object_between_tracks ()
	{
		double obj = Math.random();
		// A very small probability of returning true
		if(obj < 0.04)
		{
			return true;
		}
		return false;
	}
	
	public boolean obstacle_on_rails()
	{
		double obj = Math.random();
		// A very small probability of returning true
		if(obj < 0.03)
		{
			return true;
		}
		return false;
	}
	
	// Assuming array[0] is left and array[1] is right
	public int[][] read_infrared_cameras ()
	{
		int[][] test = new int[2][100];		
		double animal = Math.random();
		if(animal < 0.05)
		{
			File src = new File("src/ir_source/thermal.jpg");
			File dest = new File("src/infrared/thermal.jpg");
			try{
			Files.copy(src,dest);
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		else
		{
			File src = new File("src/ir_source/regular.jpg");
			File dest = new File("src/infrared/regular.jpg");
			try{
			Files.copy(src,dest);
			}
			catch(IOException e)
			{
				System.out.println(e);
			}	
		}
		return test;
	}
	
	// Assuming array[0] is left and array[1] is right
	public int[][] read_rail_cameras ()
	{
		double crack = Math.random();
		if(crack < 0.05)
		{
			File src = new File("src/uv_source/crack3.jpg");
			File dest = new File("src/uv/crack3.jpg");
			try{
			Files.copy(src,dest);
			}
			catch(IOException e)
			{
				System.out.println(e);
			}			
		}
		else
		{
			File src = new File("src/uv_source/regular.jpg");
			File dest = new File("src/uv/regular.jpg");
			try{
			Files.copy(src,dest);
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		
		return new int[2][100];
	}
	
	public String[] transceiver_in ()
	{
		// The keys are:
		// 0 - DISTANCE
		// 1 - POWER
		// 2 - ANYTHING ELSE string to parse in case of other scenarios
		index++;
		
		String[] resp = new String[3];
		resp[0] = "" + train_dist;
		if(index > max_iter)
		{
			resp[1] = "0";
		}
		
		return resp;
		
	}
	
	public boolean transceiver_out (String data)
	{
		System.out.println(data);
		return true;		
	}
	
	public boolean power(int mode)
	{
		return 1 == mode;
	}
	
	public boolean light(int light, int mode)
	{
		return true;
	}
	
	
}
