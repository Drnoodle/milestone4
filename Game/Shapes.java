package Game;

import java.util.HashSet;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PVector;
import Game.Utils.Bound;
import Game.Utils.Score;


abstract public class Shapes {

	
	
	
	
	   public static class Ball 
	   {

			     
			     
			   public final int RADIUS;
	
			   public PVector velocity, position;
			     
			
			
			   public Ball(int radius){
			   this.RADIUS  = radius;
			   this.velocity = new PVector(0,0,0);                    
			   this.position = new PVector(0, 0, 0);
			   }
			
		
			   public boolean hit(PVector thatPosition, float margin){ 
			   return this.position.dist(thatPosition) - margin <= RADIUS; 
			   }
			   
			   
			   public void updatePosition(ShapeSpace space, float rotX, float rotZ){
			          
			        
			          //------------- position
			          
			          PVector tryToMove = PVector.add( position, velocity );
			          
			          if(  !space.hit( tryToMove, RADIUS )  ) { 
			        	  
			            position = tryToMove;
			            velocity.add( 	  new PVector(   PApplet.sin(rotZ) * Utils.BALL_MASSE * Utils.GRAVITY,  
			            								 0,  
			            								 -PApplet.sin(rotX) * Utils.BALL_MASSE * Utils.GRAVITY   )   	);
			            
			            
				          PVector frictionVelocity = PVector.mult( velocity, -1);   
				          frictionVelocity.normalize(); 
				          frictionVelocity.mult(Utils.BALL_FRICTION_MAGNITUDE);
				          velocity.add(frictionVelocity); 

			            
			          }
			          else   velocity =  space.bounce( velocity , tryToMove, RADIUS);


			  }
			     
			       
			   @Override
			   public String toString(){ return "ball of radius " +  RADIUS; }
			    
	    
	   }

	   
	   
	   
	   
	   
   private interface StaticShape {
	      
   public boolean hit(PVector thatPosition, float margin);
   public PVector bounce(PVector thatVelocity, PVector thatPosition, float margin);
   
   }

   
    
   public static class Plate implements StaticShape
   {
	   
     
          public final int WIDTH, HEIGHT, DEPTH;
         
          public  PVector position;
          private final Bound BOUND_X, BOUND_Z;
  

          public Plate(int width, int height,  int depth){
          this.WIDTH  = width;
          this.HEIGHT = height; 
          this.DEPTH  =  depth;
          this.BOUND_X  = new Bound(-this.WIDTH/2, this.WIDTH/2);
          this.BOUND_Z  = new Bound(-this.DEPTH/2, this.DEPTH/2);
          this.position = new PVector(0,0,0);
          }
         
         
         private boolean outOnX(PVector thatPos, float margin){    return BOUND_X.isOut(thatPos.x-margin) || BOUND_X.isOut(thatPos.x+margin);      }
         private boolean outOnZ(PVector thatPos, float margin){    return BOUND_Z.isOut(thatPos.z-margin) || BOUND_Z.isOut(thatPos.z+margin);      } 
        

         @Override
         public boolean hit(PVector thatPosition, float margin){ 
         return outOnX(thatPosition, margin) || outOnZ(thatPosition, margin); 
         }
         
         @Override
         public PVector bounce(PVector thatVelocity, PVector thatPosition, float margin){
         if( outOnX(thatPosition, margin) ) thatVelocity.x = - thatVelocity.x;
         if( outOnZ(thatPosition, margin) ) thatVelocity.z = - thatVelocity.z;
         return thatVelocity;
         }
         
         @Override
         public String toString(){ return "plate of width : " + WIDTH + ", height : " + HEIGHT + ", DEPTH : " + DEPTH  ; }
          
             
   }

    
   
   
   
   
   public static class Cylinder implements StaticShape
   {
           
         
         public  PVector position;
         public  final float   RADIUS,    HEIGHT;
         private final float[] circleXExtrapol, circleZExtrapol;
         

         
         public Cylinder(PVector position, float cylinderBaseSize, float cylinderHeight, int cylinderResolution)
         {
                               
         this.HEIGHT  = cylinderHeight;
         this.RADIUS  = cylinderBaseSize;
         
         this.position = position;
         
         this.circleXExtrapol = new float[cylinderResolution+1];
         this.circleZExtrapol = new float[cylinderResolution+1];
                               
                               
         // initialize x,z circle point using the resolution                       
         float radius; 
         
         for(int i = 0; i< cylinderResolution + 1; i++){
         radius = (PApplet.TWO_PI/cylinderResolution)*i;     
         circleXExtrapol[i] = PApplet.sin(radius)*cylinderBaseSize;     
         circleZExtrapol[i] = PApplet.cos(radius)*cylinderBaseSize;
          }
           
                                       
         }
             
         
         public float[] xExtrapol() {		 return circleXExtrapol;    	     }
         
         public float[] zExtrapol() {		 return circleZExtrapol;	    	 }
         
         

         @Override
         public boolean hit(PVector thatPosition, float margin){ 
         return thatPosition.dist( this.position ) - margin <= RADIUS;
         }
         
         
         @Override
         public PVector bounce(PVector thatVelocity, PVector thatPosition, float margin){

         PVector normalV = PVector.sub(thatPosition, position);
         normalV.normalize();
         float VelTimesNormal = normalV.x*thatVelocity.x +  normalV.y*thatVelocity.y +  normalV.z*thatVelocity.z;
         thatVelocity = PVector.sub(thatVelocity,  PVector.mult(normalV, 2*VelTimesNormal));
         
         return thatVelocity;
         }   


         @Override
         public String toString(){ return "cylinder of radius " +  RADIUS + 
        		 						  " with height "       +  HEIGHT    ;	 }
         
         
   }
   
   
   
   
   
   
   
   
   public static class ShapeSpace implements StaticShape {

	   
	   public  PVector position;  
	   private Score score;
	   private Plate plate;
	   private Set<Cylinder> cylinders = new HashSet<Cylinder>();
	   
	   
	   
	       public ShapeSpace(PVector position, Plate plate, Score score){
	       this.plate    = plate;
	       this.position = position;
	       this.score     = score;
	       }
	   
	   
	       public void addCylinder(Cylinder cyl){    this.cylinders.add(cyl);     }

	       public Plate plate() { return plate; }
	       
	       public Score score() { return score; }
	       
	       
	   
	       
	       @Override
	       public boolean hit(PVector thatPosition, float margin){ 
	         
	       boolean hitSomething = plate.hit(thatPosition, margin);
	        
	       for(StaticShape cylinder : cylinders) hitSomething = hitSomething || cylinder.hit(thatPosition, margin);
	       
	       return hitSomething; 
	       }
	           
	             
	       @Override
	       public PVector bounce(PVector thatVelocity, PVector thatPosition, float margin){
	       
	        PVector newVel = new PVector(0,0,0);
	        

	        
	        if( plate.hit(thatPosition, margin) )   newVel = plate.bounce(thatVelocity, thatPosition, margin );
	        else                                    newVel = thatVelocity.get();
	        
	        
	        for(StaticShape cylinder : cylinders)   if( cylinder.hit(thatPosition, margin) ) 
	        newVel.add(   cylinder.bounce(thatVelocity, thatPosition, margin)  );
	        
	        
	        
	        // updating score
	        if( plate.hit(thatPosition, margin) )  score.add( - newVel.mag() );
	        else                                   score.add( + newVel.mag() );
	        
	       return newVel;
	       }
	       
	       
	       public Set<Cylinder> cylinders(){   return new HashSet<Cylinder>( cylinders);  }
	       
	   
	 }
   
   
 
   
   
   
   
   
	
   
   
}
