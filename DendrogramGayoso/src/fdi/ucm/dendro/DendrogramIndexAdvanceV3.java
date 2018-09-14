package fdi.ucm.dendro;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndexAdvanceV3 extends DendrogramIndexAdvanceV2{

	private Map<Integer,Long> snapshotTags;
    private Map<RoaringBitmap,Long> snapshotState;
	private static boolean debug=false;
	
    public DendrogramIndexAdvanceV3() {
    	super();
    	snapshotTags = new HashMap<>();
        snapshotState= new HashMap<>();
	}
    
    public DendrogramIndexAdvanceV3(DCollection collection) {
    	super();
    	snapshotTags = new HashMap<>();
        snapshotState= new HashMap<>();
	}
    
    @Override
    protected void cleanCache() {
    	// Aqui no limpio
    }
    
    @Override
    protected void actionAboveFinal(DState final1) {
    	snapshotTags.put(final1.getIde(), System.nanoTime());
    }
    
    @Override
    public RoaringBitmap getResources(List<DState> actualState) {
    	Collections.sort(actualState,comparator);
		
		
		RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
		
		Long TimeMinimo = snapshotState.get(lista);
		boolean valid=true;
		if (TimeMinimo!=null)
			for (Integer integer : lista) 
				if (snapshotTags.get(integer)!=null&&snapshotTags.get(integer)>TimeMinimo)
				{
					valid=false;
					break;
				}
		
		
		snapshotState.put(lista, System.nanoTime());
		
		RoaringBitmap Salida=null;
		
		if (valid)
			Salida=CacheResor.get(lista);
		
		
		
		if (Salida==null)
		{
		Salida=super.getResources(actualState);
		CacheResor.put(lista, Salida);
		}else
			if (debug)
				System.out.println("Cache Activa Resor");
	
	return Salida;
		
    }
    
    
    @Override
	public RoaringBitmap getSelectableTags(LinkedList<DState> actualState) {
		
		
		Collections.sort(actualState, comparator);
		
		RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
			
		Long TimeMinimo = snapshotState.get(lista);
		boolean valid=true;
		if (TimeMinimo!=null)
			for (Integer integer : lista) 
				if (snapshotTags.get(integer)!=null&&snapshotTags.get(integer)>TimeMinimo)
				{
					valid=false;
					break;
				}
		
		
		snapshotState.put(lista, System.nanoTime());
		
		RoaringBitmap Salida=null;
		
		if (valid)
			Salida=CacheSelect.get(lista);
		
		
		if (Salida==null)
		{
		Salida= super.getSelectableTags(actualState);
		CacheSelect.put(lista, Salida);
		}else
			if (debug)
				System.out.println("Cache Activa Select");
		
		return Salida;
	}
    
    @Override
    public LinkedList<DState> transit(LinkedList<DState> actualState, int tag) {
RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
		
		Long TimeMinimo = snapshotState.get(lista);
		boolean valid=true;
		if (TimeMinimo!=null)
			for (Integer integer : lista) 
				if (snapshotTags.get(integer)!=null&&snapshotTags.get(integer)>TimeMinimo)
				{
					valid=false;
					break;
				}
		
		
		snapshotState.put(lista, System.nanoTime());
		
		HashMap<Integer, LinkedList<DState>> SalidaH=null;
		
		if (valid)
			SalidaH=CacheTrans.get(lista);
		
		LinkedList<DState> Salida=null;
		
		if (SalidaH==null)
			SalidaH=new HashMap<Integer, LinkedList<DState>>();
		else
			 Salida=SalidaH.get(new Integer(tag));
		
		if (Salida==null)
		{
			Salida=super.transit(actualState, tag);
			SalidaH.put(new Integer(tag), Salida);
			CacheTrans.put(lista, SalidaH);
		}
		else
			if (debug)
				System.out.println("Cache Activa Trans"); 
		
		return Salida;
    }
}
