package fdi.ucm.dendro;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndex extends DState {

	protected DState Inicial;
	
	public DendrogramIndex(DCollection collection) {
		Inicial	= new DState();
		for (Integer doc : collection.getResources()) {
			InsertResource(doc,collection.getTagsFor(doc));
		}
	}

	public void InsertResource(Integer doc, RoaringBitmap tagsFor) {
//		LinkedList<Integer> Lista=new LinkedList<Integer>(); 
//		for (Integer integer : tagsFor) {
//			Lista.add(integer);
//		}
//		Collections.sort(Lista);
//		
		
		DState Final = Inicial;
		boolean found_=false;
		
		Set<Integer> processT= new HashSet<Integer>();
		for (Integer integer : tagsFor)
			processT.add(integer);
		
		
		
		while (!found_)
		{
			
			
			List<Integer> toExtend=new LinkedList<Integer>();
			List<Integer> newIntent=new LinkedList<Integer>();
			
			
			for (Integer integer : Final.getIntent()) 
				if (!tagsFor.contains(integer))
					toExtend.add(integer);
				else
					{
					processT.remove(integer);
					newIntent.add(integer);
					}
			
			
			if (toExtend.isEmpty()&&processT.isEmpty())
				//SOY YO
				found_=true;
			else
			{
				//Transita
				
				DState Destino=null;
				for (Integer integer : processT) {
					if (Final.getExtend().contains(integer))
						if (Destino==null)
							Destino=getDestino(Final, integer);
						else;
					else
						Final.getExtend().add(integer);
				}
				
				if (toExtend.isEmpty())
				{
					//No divido
					if (Destino!=null)
						//Transito hacia delante
						Final=Destino;
					else
						//Transito a nuevo
						{
						Final=creaNodo(Final, tagsFor);
						found_=true;
						}
				}
				else
				{
//					Divido
					DState NewStat=Final.cloneS();
					Final.getResources().clear();
					Final.getIntent().clear();
					for (Integer integer2 : newIntent) 
						Final.getIntent().add(integer2);
					for (Integer integer2 : toExtend)
						Final.getExtend().add(integer2);
					
					Final.getTransit().add(NewStat);
					
					Final=creaNodo(Final, tagsFor);
					found_=true;
					
					
				}
					
			
			
			}
			
			
			
			
			
		}
		Final.getResources().add(doc);
		
		
	}

	private DState getDestino(DState final1, Integer integer) {
		for (DState stateB : final1.getTransit()) {
			if (stateB.getIntent().contains(integer))
				return stateB;
		}
		return null;
	}

	private DState creaNodo(DState final1, RoaringBitmap tagsFor) {
		DState nuevo=new DState();
		nuevo.setIntent(tagsFor);
		final1.getTransit().add(nuevo);
		return nuevo;
	}

	public DendrogramIndex() {
		Inicial	= new DState();
	}

}