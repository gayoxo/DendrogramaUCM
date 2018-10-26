package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.roaringbitmap.RoaringBitmap;

public class DendroNavigationSystemCompleteInternalCacheL2 implements NavigationSystem {
   protected DCollection collection;
   protected DendrogramIndexCompleteCacheL2 iindex;
   protected RoaringBitmap activeTags;
   protected RoaringBitmap filteredResources;
   protected RoaringBitmap selectableTags;
   protected LinkedList<DState> ActualState;
   private Map<RoaringBitmap,RoaringBitmap> resourceSetsStore;
   private Map<RoaringBitmap,RoaringBitmap> selectableTagsStore;
   private Map<RoaringBitmap,RoaringBitmap> representativesStore;
   private Map<Integer,Long> snapshotTags;
   private Map<RoaringBitmap,Long> snapshotState;
   private final static boolean DEBUG=false;
   private Long snapshotTagsEmpty;
   

   public DendroNavigationSystemCompleteInternalCacheL2(DCollection collection,boolean vacio) {
     this.collection = collection; 
     activeTags=new RoaringBitmap();
     ActualState=new LinkedList<>();
     
     if (vacio)
    	 this.iindex = new DendrogramIndexCompleteCacheL2();
     else
    	 this.iindex = new DendrogramIndexCompleteCacheL2(collection);
     
     resourceSetsStore = new HashMap<>();
     selectableTagsStore = new HashMap<>();
     representativesStore = new HashMap<>();
     snapshotTags = new HashMap<>();
     snapshotState= new HashMap<>();
     
   }
   


	@Override
    public void init() {
		activeTags=new RoaringBitmap();
//		filteredResources = collection.getResources();
//		selectableTags = computeInitialSelectableTags();
		resourceSetsStore.put(activeTags.clone(),filteredResources);
        selectableTagsStore.put(activeTags.clone(),selectableTags);
        for(int tag: collection.getTags()) {
            snapshotTags.put(tag,System.nanoTime());
        }
        
        snapshotTagsEmpty = System.nanoTime();
        snapshotState.put(activeTags.clone(),System.nanoTime());
    }

    private RoaringBitmap computeInitialSelectableTags() {
    	RoaringBitmap Salida=new RoaringBitmap();
    	Salida.or(iindex.getInicial().getExtend());
		return Salida;
	}



	@Override
    public void run(NavigationAction a) {
    	if (a.isInsert())
    	{
    		collection.addObject(a.getResource(), a.getTagsFor());
    		collection.addTags(a.getTagsFor());
    		iindex.InsertResource(a.getResource(), a.getTagsFor());
            
    		for (int tag : a.getTagsFor()) 
    			snapshotTags.put(tag, System.nanoTime());
    		
    		snapshotTagsEmpty = System.nanoTime();
    		
    	}else
    		if (a.isDelete())
    		{
    			RoaringBitmap tagsfor=collection.getTagsFor(a.getResource());
    			iindex.DeleteResource(a.getResource(), tagsfor,collection);
    			collection.removeObject(a.getResource(), tagsfor);
    	        
    			for (int tag : tagsfor) 
        			snapshotTags.put(tag, System.nanoTime());
    			
    			snapshotTagsEmpty = System.nanoTime();
    		}
    		else
    			if (a.isAdd())
    			{
    				activeTags.add(a.getTag());
    				
//    				
//    				if (ActualState.isEmpty())
//    					ActualState.add(iindex.getInicial());
//    				
//    				ActualState=iindex.transit(ActualState,a.getTag());
//    				
//    				filteredResources = iindex.getResources(ActualState);
//    		        selectableTags = iindex.getSelectableTags(ActualState);
    			}
    			else
    				if (a.isRemove())
    				{
    					 activeTags.remove(a.getTag());
    					 
//    					 ActualState.clear();
//    					 ActualState.add(iindex.getInicial());
//    					 for (Integer dState : activeTags) {
//    						 ActualState=iindex.transit(ActualState,dState);
//    					 }
//    					 
//    					 filteredResources = iindex.getResources(ActualState);
//    	    		     selectableTags = iindex.getSelectableTags(ActualState);
    				}
    	
    	
    	if (a.isAdd()||a.isRemove()) {
            
    		
    		if (a.isAdd())
     	   {
     		   if (ActualState.isEmpty())
					ActualState.add(iindex.getInicial());
				
					ActualState=iindex.transit(ActualState,a.getTag());
     	   }
     	   //a.isRemove()
            else 
     	   {
         	   ActualState.clear();
					 ActualState.add(iindex.getInicial());
					 for (Integer dState : activeTags) {
						 ActualState=iindex.transit(ActualState,dState);
					 }
         	   
     	   }
    		
    		
    		
            Long updatingTime = snapshotState.get(activeTags);   
            
            if(updatingTime != null && updated(updatingTime,activeTags)) {
              if(DEBUG) {
                System.out.println("***** "+activeTags+" CACHED");  
              } 
              filteredResources = resourceSetsStore.get(activeTags);
              selectableTags = selectableTagsStore.get(activeTags); 
           }
           else {
        	   
        	   
        	   
              RoaringBitmap atags = activeTags.clone();
              
              
              filteredResources = iindex.getResources(ActualState);

              
              
              RoaringBitmap representative = representativesStore.get(filteredResources);
              
              if(representative != null && updated(snapshotState.get(representative), selectableTagsStore.get(representative))) { 
                 selectableTags = selectableTagsStore.get(representative);    
              }
              else {
            	  
            	  selectableTags = iindex.getSelectableTags(ActualState);
            	  
                representativesStore.put(filteredResources,atags);
              }
              resourceSetsStore.put(atags,filteredResources);
              selectableTagsStore.put(atags,selectableTags);
              snapshotState.put(atags, System.nanoTime());
            }
    	}
    	
    }

    	
    	 private boolean updated(Long timeSet, RoaringBitmap tagsFor) {
    		 
    		 if (activeTags.isEmpty())
    	    		return snapshotTagsEmpty < timeSet;
    		 
             for(int tag: tagsFor) {
                 if(snapshotTags.get(tag) > timeSet) return false;
             }
             return true;
     }       


	@Override
    public RoaringBitmap getFilteredResources() {
        return filteredResources;
    }

    @Override
    public RoaringBitmap getActiveTags() {
        return activeTags;
    }

    @Override
    public RoaringBitmap getSelectableTags() {
        return selectableTags;
    }

   
   
    
}
