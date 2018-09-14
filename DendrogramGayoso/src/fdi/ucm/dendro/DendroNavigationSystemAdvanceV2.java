package fdi.ucm.dendro;

public class DendroNavigationSystemAdvanceV2 extends DendroNavigationSystem {

	
	public DendroNavigationSystemAdvanceV2(DCollection collection, boolean vacio) {
		super(collection, vacio);
	}

	@Override
	 protected void createindex(boolean vacio) {
  	   if (vacio)
  	    	 this.iindex = new DendrogramIndexAdvanceV2();
  	     else
  	    	 this.iindex = new DendrogramIndexAdvanceV2(collection);
	
	 }
   

    
}
