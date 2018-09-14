package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndexAdvanceV2 extends DendrogramIndexAdvance{


	protected HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>> CacheTrans;
	private static boolean debug=false;

	public DendrogramIndexAdvanceV2() {
		super();
		CacheTrans=new HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>>();
	}
	
	public DendrogramIndexAdvanceV2(DCollection collection) {
		super(collection);
		CacheTrans=new HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>>();
	}

	@Override
	public void InsertResource(Integer doc, RoaringBitmap tagsFor) {
		super.InsertResource(doc, tagsFor);
	}
	
	@Override
	public void DeleteResource(int resource, RoaringBitmap tagsFor, DCollection collection) {
		super.DeleteResource(resource, tagsFor, collection);
		
	}

	@Override
	protected void cleanCache() {
		super.cleanCache();
		CacheTrans=new HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>>();
	}
	
	@Override
	public LinkedList<DState> transit(List<DState> actualState, int tag) {
		
		LinkedList<DState> Salida=null;
		
		if (actualState.size()!=1)
		{
	
		
		RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
		
		HashMap<Integer, LinkedList<DState>> SalidaH=CacheTrans.get(lista);
		
		
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
		else
			return super.transit(actualState.get(0), tag);
	}
	



	
	
}
