


import processing.core.PApplet;
import processing.video.Movie;
import Game.GameInfo;
import Game.GameManager;
import Game.Shapes.Ball;
import Game.Shapes.Plate;
import Game.Shapes.ShapeSpace;
import Game.Utils;
import Game.Utils.Mode;
import Game.Utils.Score;
import ImageDetection.BoardDetection;

public class TangibleGame extends PApplet {


	
	private static final long serialVersionUID = 1L;

	Mode mode				=   new Mode();
    
    Score score        		=   new Score();
	
	Plate plate 			= 	new Plate(Utils.PLATE_WIDTH, Utils.PLATE_HEIGHT,  Utils.PLATE_DEPTH);
	
    ShapeSpace shapeSpace 	= 	new ShapeSpace(Utils.SCENE_CENTER, plate, score);
    
    Ball ball   			= 	new Ball(Utils.BALL_RADIUS);
    
    GameManager manager;
    
    GameInfo gameInfo;
    
	Movie movie;
	
	BoardDetection boardDetection;
    

	
	
    @Override
    public void setup() { 
    size(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT, P3D);
    noStroke();
    
    manager		   = 	new GameManager(shapeSpace, ball, this, mode );
    gameInfo       =    new GameInfo(this, shapeSpace, ball, score);
	boardDetection =    new BoardDetection(this);
	movie 		   =    new Movie(this, "data/testvideo.mp4");

	movie.loop();
	
    gameInfo.background.makeImg();
    }
    
    
    

    public void draw() {  

    // ambiance
    	
    background(230,230,230);    
    lights();   
    
    // game 
    
    boardDetection.updateAnglesIfDetected(movie);
    manager.rotatedDraw( boardDetection.rotX() , boardDetection.rotZ() );
    
    
    // info
    
    gameInfo.barChartInfo.makeImg();
    gameInfo.plateInfo.makeImg();
    gameInfo.scoreInfo.makeImg();

    image( gameInfo.background.getGraphics(),   gameInfo.background.position().x,   gameInfo.background.position().y );
    image( gameInfo.barChartInfo.getGraphics(), gameInfo.barChartInfo.position().x, gameInfo.barChartInfo.position().y );
    image( gameInfo.plateInfo.getGraphics(),    gameInfo.plateInfo.position().x,    gameInfo.plateInfo.position().y );
    image( gameInfo.scoreInfo.getGraphics(),    gameInfo.scoreInfo.position().x,    gameInfo.scoreInfo.position().y );
    
    
    }
    
    
    
	
    public void keyPressed()   {        if(key == CODED && keyCode == SHIFT)      mode.setBuilding();        			}
    
    public void keyReleased()  {        if(key == CODED && keyCode == SHIFT)      mode.setPlaying();         			}
    
    public void mousePressed() {        if(mode.isBuilding()) 					  manager.buildCylinderBuilder();       }

    


	void movieEvent(Movie m)   {			  m.read();				}
	

	
	
}




