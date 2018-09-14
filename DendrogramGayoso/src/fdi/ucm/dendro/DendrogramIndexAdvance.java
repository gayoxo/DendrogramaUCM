package fdi.ucm.dendro;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndexAdvance extends DendrogramIndex{


	private HashMap<RoaringBitmap,RoaringBitmap> CacheResor;
	private HashMap<RoaringBitmap,RoaringBitmap> CacheSelect;
	private static boolean debug=false;
	protected Comparator<DState> comparator=new Comparator<DState>() {

		@Override
		public int compare(DState o1, DState o2) {
			return o2.getIde()-o1.getIde();
		}
	};

	public DendrogramIndexAdvance() {
		super();
		CacheResor=new HashMap<RoaringBitmap,RoaringBitmap>();
		CacheSelect=new HashMap<RoaringBitmap,RoaringBitmap>();
	}
	
	public DendrogramIndexAdvance(DCollection collection) {
		super(collection);
		CacheResor=new HashMap<RoaringBitmap,RoaringBitmap>();
		CacheSelect=new HashMap<RoaringBitmap,RoaringBitmap>();
	}

	@Override
	public void InsertResource(Integer doc, RoaringBitmap tagsFor) {
		super.InsertResource(doc, tagsFor);
		CacheResor=new HashMap<RoaringBitmap,RoaringBitmap>();
		CacheSelect=new HashMap<RoaringBitmap,RoaringBitmap>();
	}
	
	@Override
	public void DeleteResource(int resource, RoaringBitmap tagsFor, DCollection collection) {
		super.DeleteResource(resource, tagsFor, collection);
		CacheResor=new HashMap<RoaringBitmap,RoaringBitmap>();
		CacheSelect=new HashMap<RoaringBitmap,RoaringBitmap>();
	}

	@Override
	public RoaringBitmap getResources(List<DState> actualState) {
		
		Collections.sort(actualState,comparator);
		
		
		RoaringBitmap lista=new RoaringBitmap();
		
		for (DState integer : actualState) 
			lista.add(integer.getIde());
		
		RoaringBitmap Salida=CacheResor.get(lista);
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
			
		RoaringBitmap Salida=CacheSelect.get(lista);
		if (Salida==null)
		{
		Salida= super.getSelectableTags(actualState);
		CacheSelect.put(lista, Salida);
		}else
			if (debug)
				System.out.println("Cache Activa Select");
		
		return Salida;
	}
	
	
	
}
