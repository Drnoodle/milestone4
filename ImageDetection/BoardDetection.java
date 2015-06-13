package ImageDetection;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class BoardDetection {


	private ImageFilter filter;
	
	private List<PVector> lastIntersections = new ArrayList<>();
	
	private PVector rotation = new PVector(0,0,0);
	
	
	public BoardDetection(PApplet applet){
		this.filter = new FilterLibrary(applet).getFilterForDetection();	
	}
	
	
	
	
	
	public BoardDetection updateAnglesIfDetected(PImage image){
		
		if(image.height <=  FilterLibrary.KERNEL_MATRIX_MAX_SIZE || image.width <= FilterLibrary.KERNEL_MATRIX_MAX_SIZE ) 
		return this;	
			
		
		image = filter.convertImage(image);
		
	    Hough hough = new Hough(image).neighbourhoodFilter().takeBest(4);
	    
	    
	    
	    if(  hough.size() == 4  ){
	    	
	    List<PVector> lines =  hough.vectlines();
	    
	    QuadGraph quads = new QuadGraph();
	    quads.build( lines, image.width, image.height);
	    quads.findCycles(lines);
	    
		for(int[]quad: quads.cycles){
			
	    	PVector l1=lines.get(quad[0]);
	    	PVector l2=lines.get(quad[1]);
	    	PVector l3=lines.get(quad[2]);
	    	PVector l4=lines.get(quad[3]);

	    	PVector c12=Line.intersections( l1, l2);
	  	    PVector c23=Line.intersections(l2,l3);
	  	    PVector c34=Line.intersections(l3, l4);
	  	    PVector c41=Line.intersections(l4,l1);
	  	    
	  	    lastIntersections.removeAll(lastIntersections);
	  	    lastIntersections.add(c23);
	  	  	lastIntersections.add(c12);
	  	  	lastIntersections.add(c41);
	  	  	lastIntersections.add(c34);
	  	  	lastIntersections = CWComparator.sortCorners(lastIntersections);
	    
		}
		
		rotation = new TwoDThreeD(image.width, image.height).get3DRotations(lastIntersections);
	    
	    }
	    
	    
	    return this;
	}
	
	
	
	public List<PVector> intersections(){		return lastIntersections;		}

	public float rotX()					{		return rotation.x;				}
	
	public float rotZ()					{		return rotation.y;				}
	
	
}
