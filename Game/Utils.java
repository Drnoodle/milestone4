package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Utils {
	  
	
	
	
	

    // 1. windows & scene



    public final static int WINDOW_WIDTH  = 800;
    public final static int WINDOW_HEIGHT = 500;
    public final static PVector SCENE_CENTER = new PVector(WINDOW_WIDTH/2, WINDOW_HEIGHT/2+20, -400);




    // 2. plate



    public final static int   PLATE_HEIGHT = 12; 
    public final static int   PLATE_WIDTH  = 800;
    public final static int   PLATE_DEPTH  = 800; 






    // 3. ball



    public final static int   BALL_RADIUS = 20; 
    public final static float BALL_MASSE = 0.1f;
    







    // 4. forces 
    
    
    
    public final static float GRAVITY = 9.81f;
    public final static float BALL_MU = 0.12f;
    public final static float BALL_NORMAL_FORCE = 1;
    public final static float BALL_FRICTION_MAGNITUDE = BALL_NORMAL_FORCE * BALL_MU; 






    // 5. cylinder 
     
     
     
    public final static float     CYLINDER_BASE_SIZE = 30;
    public final static float     CYLINDER_HEIGHT = 20;
    public final static int       CYLINDER_RESOLUTION = 100;   




    // 6. informations


    public final static int   HEIGHT_PANEL_INFORMATION = 100;





    // 7. 2d distance 



    public static float  distance(float x1, float y1, float x2, float y2){
    return PApplet.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)  );
    }


	
	
	
	
	public static class Bound {

		
		
		public final float LOWER_BOUND, UPPER_BOUND;

		
		
		public Bound(float lowerBound, float upperBound) {
			this.LOWER_BOUND = lowerBound;
			this.UPPER_BOUND = upperBound;
		}

		
		
		
		
		
		public float getValueInside(float value) {
			
			return Math.max(    Math.min(value, UPPER_BOUND)   , LOWER_BOUND);
			
		}
		

		public float distanceToInside(float value) {

			if (value < LOWER_BOUND)		return LOWER_BOUND - value;
			else if (value > UPPER_BOUND)	return value - UPPER_BOUND;
			else							return 0.0f;

		}
		

		public boolean isOut(float x) {
			return x < LOWER_BOUND || UPPER_BOUND < x;
		}

		
		
		
		@Override
		public String toString() {
			return "bound : [" + LOWER_BOUND + "," + UPPER_BOUND + "]";
		}

		
		
	}
	
	
	
	
	
	
	
	
	public static class Score {
	
	 private float score = 0;
	 private float maxScore = 0; 
	 private float minScore = 0;
 
	 private LinkedList<Float> history = new LinkedList<Float>();
 
	 
	 public void add(float value){ 
		 
	 if( Math.abs(value) > 3){  // threshold for adding velocity history
	 score += value;
	 
	 if(score > maxScore) 	   maxScore = score;
	 if(score < minScore) 	   minScore = score;
	 
	 if(history.size() > 100){ 
		 Float lastValue = history.pollLast();
		 if(lastValue == minScore ) minScore = Collections.min(history);
		 if(lastValue == maxScore ) maxScore = Collections.max(history);
	 }
		 
	 history.add(score);
	 
	 }

	 }
	 
	 
	 public float minScore()       {   return minScore;                          }
	 
	 public float maxScore()       {   return maxScore;                          }
	 
	 public int size()             {   return history.size();                    }
	 
	 public List<Float> history()  {  return new ArrayList<>(history);			 }
	 
	 public float score()          {   return score;                             }
	 
	 
	}
	
	
	
	
	
	

	public static class Mode
	{
		
	private boolean playing = true, building = false;
	
	
	public void setBuilding()  {     playing = false;    /**  AND  **/    building = true;              } 
	
	public void setPlaying()   {     playing = true;     /**  AND  **/    building = false;             }
	
	public boolean isPlaying() {     return playing;     												}
	
	public boolean isBuilding(){     return building;     												}
	
	
	@Override
	public String toString(){ if(playing) return "mode : playing";  else return "mode : building";  }
	
	}



	
	
}