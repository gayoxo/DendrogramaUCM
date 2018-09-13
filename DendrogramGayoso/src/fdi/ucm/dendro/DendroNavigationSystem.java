package fdi.ucm.dendro;

import java.util.LinkedList;

import org.roaringbitmap.RoaringBitmap;

public class DendroNavigationSystem implements NavigationSystem {
   protected DCollection collection;
   protected DendrogramIndex iindex;
   protected RoaringBitmap activeTags;
   protected RoaringBitmap filteredResources;
   protected RoaringBitmap selectableTags;
   protected LinkedList<DState> ActualState;
   
   

   public DendroNavigationSystem(DCollection collection,boolean vacio) {
     this.collection = collection; 
     activeTags=new RoaringBitmap();
     ActualState=new LinkedList<>();
     
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
    		else
    			if (a.isAdd())
    			{
    				activeTags.add(a.getTag());
    				if (ActualState.isEmpty())
    					ActualState.add(iindex.getInicial());
    				
    				ActualState=iindex.transit(ActualState,a.getTag());
    				
    				filteredResources = iindex.getResources(ActualState);
    		        selectableTags = iindex.getSelectableTags(ActualState);
    			}
    			else
    				if (a.isDelete())
    				{
    					 activeTags.remove(a.getTag());
    					 
    					 ActualState.clear();
    					 ActualState.add(iindex.getInicial());
    					 for (Integer dState : activeTags) {
    						 ActualState=iindex.transit(ActualState,dState);
    					 }
    					 
    					 filteredResources = iindex.getResources(ActualState);
    	    		     selectableTags = iindex.getSelectableTags(ActualState);
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
