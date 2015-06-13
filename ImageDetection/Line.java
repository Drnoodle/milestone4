package ImageDetection;
import processing.core.PVector;



public class Line implements Comparable<Line>{
		
	
		public final int accPhi; 
		public final int accR;
		public final float r;
		public final float phi;
		public final int rDim;
		private int votes;
		
		
		public Line(int accPhi, float phi, int accR, float r, int rDim ){
			this.votes = 0;
			this.accPhi = accPhi;
			this.accR = accR;
			this.r = r;
			this.phi = phi;
			this.rDim = rDim;
		}
		

		
		public static PVector intersections(PVector l1, PVector l2){
				
			double d = Math.cos(l2.y)*Math.sin(l1.y) - Math.cos(l1.y)*Math.sin(l2.y);
			double x =  ( l2.x*Math.sin(l1.y) - l1.x*Math.sin(l2.y) )/d;
			double y =  (-l2.x*Math.cos(l1.y) + l1.x*Math.cos(l2.y) )/d;
				
			return new PVector((int)x,(int)y)  ;
			
		}
				
		

		
		public int indice()		{	return (accPhi + 1) * (rDim + 2) + accR + 1;		}
		
		
		public void addVote()	{ 	votes++; 											}
		
		
		public int votes()		{   return votes; 										}
		
		
		public PVector toVect()	{   return new PVector(r,phi);							}
		
		
		
		
		@Override
		public int compareTo(Line that) {
			if(this.votes > that.votes) return 1;
			else if(this.votes == that.votes) return 0;
			else return -1;
		}	
	

		
	}
	
	
	
	
	