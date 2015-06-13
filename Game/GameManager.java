package Game;

import processing.core.PApplet;
import processing.core.PVector;
import Game.Shapes.Ball;
import Game.Shapes.Cylinder;
import Game.Shapes.Plate;
import Game.Shapes.ShapeSpace;
import Game.Utils.Mode;


public class GameManager {

	
    
    
	private final int BALL_COLOR;
	private final int PLATE_COLOR;
	private final int CYLINDER_COLOR;
	
	private ShapeSpace shapeSpace;
	private Ball ball;
	private PApplet applet;
	private Cylinder buildingCylinder;
	private Mode mode;
	
	
	
	public GameManager(ShapeSpace shapeSpace, Ball ball, PApplet applet, Mode mode){
		this.shapeSpace = shapeSpace;
		this.ball       = ball;
		this.applet     = applet;
		this.mode       = mode;
		
		this.BALL_COLOR = applet.color(255,0,0);
		this.PLATE_COLOR = applet.color(255,255,255);
		this.CYLINDER_COLOR = applet.color(70,70,70);
		this.buildingCylinder = new Cylinder( new PVector(0,0,0), Utils.CYLINDER_BASE_SIZE, Utils.CYLINDER_HEIGHT, Utils.CYLINDER_RESOLUTION);
	}
	
	
	
	
	
	public void rotatedDraw( float rotX, float rotZ ){
	    applet.pushMatrix();
	    
	    applet.translate(shapeSpace.position.x, shapeSpace.position.y, shapeSpace.position.z);
	    applet.rotateX( rotX );  
	    applet.rotateZ( rotZ ); 
	    
	    
	    if( mode.isPlaying()  )  ball.updatePosition(shapeSpace, rotX, rotZ);
	    if( mode.isBuilding() )  horizontalDrawCylinderBuilder();
	    
	    horizontalDraw(ball);
	    horizontalDraw(shapeSpace.plate());
		for( Cylinder cylinder : shapeSpace.cylinders() ) horizontalDraw(cylinder);

	    applet.popMatrix();
	}
	
	
	
	
	
	private void horizontalDraw(Ball ball){
		
	     applet.pushMatrix();
	     applet.translate( ball.position.x,  ball.position.y - ball.RADIUS/2 - shapeSpace.plate().HEIGHT/2, ball.position.z);
	     applet.fill(BALL_COLOR); 
	     applet.sphere(ball.RADIUS);  
	     applet.popMatrix();
	     
	}
	
	
	
	private void horizontalDraw(Plate plate){
	    applet.pushMatrix();
	    applet.fill(PLATE_COLOR); 
	    applet.box( Utils.PLATE_WIDTH, Utils.PLATE_HEIGHT, Utils.PLATE_DEPTH);
	    applet.popMatrix();
	}
	
	
	
	
	public void horizontalDraw(Cylinder cylinder){
		
	    applet.fill(CYLINDER_COLOR);
	     
	    applet.pushMatrix(); 
        applet.translate(cylinder.position.x, -cylinder.position.y - (cylinder.HEIGHT + shapeSpace.plate().HEIGHT)/2, cylinder.position.z);
	    applet.rotateX(PApplet.HALF_PI);
	    
	    float[] xExtrapol = cylinder.xExtrapol();
	    float[] zExtrapol = cylinder.zExtrapol();
	    
	    
	    // create cylinder
	    
	    applet.beginShape(PApplet.QUAD_STRIP);  
	    for(int i = 0; i < xExtrapol.length; i++){   
	    applet.vertex( xExtrapol[i], zExtrapol[i], -Utils.CYLINDER_HEIGHT/2 );     
	    applet.vertex( xExtrapol[i], zExtrapol[i], Utils.CYLINDER_HEIGHT/2 );    
	    }
	    applet.endShape(); 
	     
	     
	    // and circles at the top and the bottom
	    
	    applet.beginShape(PApplet.TRIANGLES);
	    for(int i = 0; i < xExtrapol.length-1; i++){  
	     // bottom
	    applet.vertex( 0, 0, -Utils.CYLINDER_HEIGHT/2 );              
	    applet.vertex( xExtrapol[i], zExtrapol[i],     -Utils.CYLINDER_HEIGHT/2);                     
	    applet.vertex( xExtrapol[i+1], zExtrapol[i+1], -Utils.CYLINDER_HEIGHT/2);
	    // top
	    applet.vertex( 0, 0, Utils.CYLINDER_HEIGHT/2 );  
	    applet.vertex( xExtrapol[i], zExtrapol[i],  Utils.CYLINDER_HEIGHT/2);    
	    applet.vertex( xExtrapol[i+1], zExtrapol[i+1],  Utils.CYLINDER_HEIGHT/2); 
	    }
	    applet.endShape(); 
	     
	    applet.popMatrix();      
		
	}
	
	
	
	
	
	  private void horizontalDrawCylinderBuilder(){    

		  float nextX =  ( applet.mouseX / (float)Utils.WINDOW_WIDTH  -  0.5f ) * shapeSpace.plate().WIDTH; 
		  float nextZ =  ( applet.mouseY / (float)Utils.WINDOW_HEIGHT -  0.5f ) * shapeSpace.plate().DEPTH;
		  PVector position = new PVector(nextX,0,nextZ);
		  
		  if( !ball.hit( position, 30) ) buildingCylinder.position = position;

		  horizontalDraw(buildingCylinder);

	  }
	  
	  
	  
	  public void buildCylinderBuilder(){
		  
		  shapeSpace.addCylinder( new Cylinder(buildingCylinder.position, Utils.CYLINDER_BASE_SIZE, Utils.CYLINDER_HEIGHT, Utils.CYLINDER_RESOLUTION ) );
		  
	  }


    
    
    

	   
}
