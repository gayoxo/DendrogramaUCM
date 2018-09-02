package fdi.ucm.dendro;

import org.roaringbitmap.RoaringBitmap;

public class DendroNavigationSystem implements NavigationSystem {
   protected DCollection collection;
   protected DState Inicial;
   
   

   public DendroNavigationSystem(DCollection collection,boolean vacio) {
     this.collection = collection; 
     
     Inicial=new DState();
   }
   
       @Override
    public void init() {

    }

    @Override
    public void run(NavigationAction a) {
        
    }

    @Override
    public RoaringBitmap getFilteredResources() {
        return null;
    }

    @Override
    public RoaringBitmap getActiveTags() {
        return null;
    }

    @Override
    public RoaringBitmap getSelectableTags() {
        return null;
    }

   
   
    
}
