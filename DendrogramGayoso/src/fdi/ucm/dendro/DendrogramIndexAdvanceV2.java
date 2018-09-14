package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.LinkedList;
import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndexAdvanceV2 extends DendrogramIndexAdvance{


	private HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>> CacheTrans;
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
		CacheTrans=new HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>>();
	}
	
	@Override
	public void DeleteResource(int resource, RoaringBitmap tagsFor, DCollection collection) {
		super.DeleteResource(resource, tagsFor, collection);
		CacheTrans=new HashMap<RoaringBitmap,HashMap<Integer,LinkedList<DState>>>();
	}

	
	@Override
	public LinkedList<DState> transit(LinkedList<DState> actualState, int tag) {
		
		RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
		
		HashMap<Integer, LinkedList<DState>> SalidaH=CacheTrans.get(lista);
		
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