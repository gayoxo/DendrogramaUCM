package fdi.ucm.dendro;

public class DendroNavigationSystemAdvanceV3 extends DendroNavigationSystem {

	
	public DendroNavigationSystemAdvanceV3(DCollection collection, boolean vacio) {
		super(collection, vacio);
	}

	@Override
	 protected void createindex(boolean vacio) {
  	   if (vacio)
  	    	 this.iindex = new DendrogramIndexAdvanceV3();
  	     else
  	    	 this.iindex = new DendrogramIndexAdvanceV3(collection);
	
	 }
   

    
}
