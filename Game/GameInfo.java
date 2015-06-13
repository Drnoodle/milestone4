package Game;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import Game.Shapes.Ball;
import Game.Shapes.Cylinder;
import Game.Shapes.ShapeSpace;
import Game.Utils.Score;


public class GameInfo {

	
	
	
	PApplet applet;
	
	public InformationLayer background; 
	public InformationLayer plateInfo;
	public InformationLayer scoreInfo;
	public InformationLayer barChartInfo;
	
	
	
	
	public GameInfo(PApplet applet, ShapeSpace shapeSpace, Ball ball, Score score){
		this.applet = applet;
		
		 PVector backgroundPosition =  new PVector( 0, Utils.WINDOW_HEIGHT - 100);
	     PVector platePosition      =  PVector.add( backgroundPosition , new PVector(5,5)					   );
	     PVector scorePosition      =  PVector.add( platePosition, new PVector( PlateInfo.WIDTH+5 , 0) );
	     PVector barChartPosition  =  PVector.add( scorePosition, new PVector( ScoreInfo.WIDTH+5 , 0) );
	    
	     background   = new Background(backgroundPosition);
	     plateInfo    = new PlateInfo(platePosition, shapeSpace, ball, applet.color(255,0,0));
	     scoreInfo    = new ScoreInfo(scorePosition, ball, shapeSpace);
	     barChartInfo = new BarChartInfo(barChartPosition, score);
	     
	}
	
	
	
	
	
	 public interface InformationLayer {		 
		 
		 public void makeImg();	
		 public PVector position();
		 public PGraphics getGraphics();
		 
	 }

	 

	 
	 
	  public class PlateInfo implements InformationLayer {
	    
		  
		  
		  
	  public  static final int WIDTH = 90, HEIGHT = 90;
	  private PGraphics plateRender;
	  private PVector POSITION;
	  private Ball ball;
	  private ShapeSpace shapeSpace;
	  private PVector realDimension; 
	  private final int BALL_COLOR;
	  private float scaledBallRadius = (float)Utils.BALL_RADIUS/Utils.PLATE_WIDTH*WIDTH;
	  private float scaledCylRadius  = (float)Utils.CYLINDER_BASE_SIZE/Utils.PLATE_WIDTH*WIDTH; 
	  
	  
	  public PlateInfo(PVector position, ShapeSpace shapeSpace, Ball ball, int ballColor){
	    this.POSITION    = position;
	    this.plateRender = applet.createGraphics(WIDTH, HEIGHT);
	    this.ball 		 = ball;
	    this.shapeSpace  = shapeSpace;
	    this.realDimension = new PVector(shapeSpace.plate().WIDTH, shapeSpace.plate().DEPTH );
	    this.BALL_COLOR    = ballColor;
	  }

	      
	    private float posOnVisualX(float x) {   return   WIDTH*(realDimension.x/2 + x)/realDimension.x;         		}
	    
	    private float posOnVisualY(float y) {   return   HEIGHT - HEIGHT*(realDimension.y/2 + y)/realDimension.y;     	}
	  
	    
	  	@Override
	    public PGraphics getGraphics()		{ 	return plateRender;    											}
	    
	  	@Override
	    public PVector   position()   		{ 	return POSITION.get(); 											}
	  
	  	@Override
	    public void makeImg(){
	      
	        plateRender.beginDraw(); 
	        
	        plateRender.background(80);
	        
	        plateRender.noStroke();
	        plateRender.fill(BALL_COLOR);
	        plateRender.ellipse( posOnVisualX( ball.position.x ) , posOnVisualY( -ball.position.z ) , scaledBallRadius , scaledBallRadius );    
	        
	        plateRender.fill(30);
	        
	        for( Cylinder cylinder : shapeSpace.cylinders() )
	        plateRender.ellipse( posOnVisualX( cylinder.position.x ), posOnVisualY( cylinder.position.z ), scaledCylRadius, scaledCylRadius );    
	        
	        plateRender.endDraw(); 
	        
	      }
	      
	      
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  public class ScoreInfo implements InformationLayer {
	    
		  
		  
	  public  static final int WIDTH = 90, HEIGHT = 90;
	  private Ball ball;
	  private ShapeSpace shapeSpace;
	  private PGraphics infoRender;
	  private PVector POSITION;

 
	  
	  public ScoreInfo(PVector position, Ball ball, ShapeSpace space){
	    this.ball       = ball;
	    this.shapeSpace = space;
	    this.POSITION   = position;
	    this.infoRender = applet.createGraphics(WIDTH, HEIGHT);
	  }
	  
	  
	  @Override
	  public PVector position()			{ 	return POSITION.get(); 			}
	  	
	  @Override
	  public PGraphics getGraphics()	{ 	return infoRender; 				}
	  	
	  @Override
	  public void makeImg(){
	      
	        infoRender.beginDraw();

	        infoRender.background(50);
	        infoRender.fill(240);
	        infoRender.text("velocity : ", 5, 15);
	        infoRender.fill(255);
	        infoRender.text(""+ Math.round( ball.velocity.mag() * 100) / 100, 5, 35);
	        infoRender.fill(240);
	        infoRender.text("score : ", 5, 60); 
	        infoRender.fill(255);
	        infoRender.text(""+shapeSpace.score().score(), 5, 80);
	        infoRender.endDraw();
	        
	    }
	      
	      
	      
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  public class Background implements InformationLayer {
	    
	    public static final int WIDTH = Utils.WINDOW_WIDTH, HEIGHT = 100;
	    private PVector POSITION;
	    private PGraphics containerRender;
	  
	    
	    
	    public Background(PVector position){
	    this.POSITION = position;
	    this.containerRender = applet.createGraphics(WIDTH, HEIGHT); 
	    }
	    
	    
	  	@Override
	    public PVector position()		{ 	return POSITION.get(); 			}

	  	@Override
	    public PGraphics getGraphics()	{ 	return containerRender; 		}

	  	@Override
	    public void makeImg(){
	    containerRender.beginDraw(); 
	    containerRender.background(30);
	    containerRender.endDraw();
	    }
	    
	    
	  }
	  




	  public class BarChartInfo implements InformationLayer{
	  
	    public static final int WIDTH = Utils.WINDOW_WIDTH -  ScoreInfo.WIDTH - PlateInfo.WIDTH - 20 ,  HEIGHT = 90;
	    private PVector POSITION;
	    private PGraphics barChartRender;
	    private Score score;
	    
	    public BarChartInfo(PVector position, Score score){
	    this.POSITION = position;
	    this.barChartRender = applet.createGraphics(WIDTH, HEIGHT); 
	    this.score = score;
	    }
	    
	  	@Override
	    public PVector position()		{ 	return POSITION.get(); 		}
	    
	  	@Override
	    public PGraphics getGraphics()	{ 	return barChartRender; 		}
	  
	  	@Override
	    public void makeImg(){
	    	
	    barChartRender.beginDraw(); 
	    barChartRender.background(50);
	    List<Float> history = score.history();
	    
	    float pieceWidth  = Math.min(   (float)WIDTH  / Math.max(1, history.size()),   WIDTH/20   );

	    
	    int x = 0, y = HEIGHT; 
	    barChartRender.noStroke();
	    barChartRender.fill(245, 80, 35, 200);
	    for( float v : history){
	    barChartRender.rect( x, y, pieceWidth, - (  (v - score.minScore())/Math.max(1,score.maxScore() - score.minScore())  )*HEIGHT  );
	    x += pieceWidth;
	    }
	    
	    barChartRender.endDraw();
	    }
	    
	    
	  
	  }
	  
	
	
	
	
	
}
