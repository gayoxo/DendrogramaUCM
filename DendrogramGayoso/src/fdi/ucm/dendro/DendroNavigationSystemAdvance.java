package fdi.ucm.dendro;

public class DendroNavigationSystemAdvance extends DendroNavigationSystem {

	
	public DendroNavigationSystemAdvance(DCollection collection, boolean vacio) {
		super(collection, vacio);
	}

	@Override
	 protected void createindex(boolean vacio) {
  	   if (vacio)
  	    	 this.iindex = new DendrogramIndexAdvance();
  	     else
  	    	 this.iindex = new DendrogramIndexAdvance(collection);
	
	 }
   

    
}
