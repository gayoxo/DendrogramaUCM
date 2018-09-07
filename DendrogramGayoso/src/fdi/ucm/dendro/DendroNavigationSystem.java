package fdi.ucm.dendro;

import org.roaringbitmap.RoaringBitmap;

public class DendroNavigationSystem implements NavigationSystem {
   protected DCollection collection;
   protected DendrogramIndex iindex;


   
   

   public DendroNavigationSystem(DCollection collection,boolean vacio) {
     this.collection = collection; 
     
     
     if (vacio)
    	 this.iindex = new DendrogramIndex();
     else
    	 this.iindex = new DendrogramIndex(collection);
   }
   
       @Override
    public void init() {

    }

    @Override
    public void run(NavigationAction a) {
    	if (a.isInsert())
    	{
    		collection.addObject(a.getResource(), a.getTagsFor());
    		collection.addTags(a.getTagsFor());
    		iindex.InsertResource(a.getResource(), a.getTagsFor());
    	}else
    		if (a.isDelete())
    		{
    			iindex.DeleteResource(a.getResource(), collection.getTagsFor(a.getResource()),collection);
    			collection.removeObject(a.getResource(), collection.getTagsFor(a.getResource()));
    		}
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
