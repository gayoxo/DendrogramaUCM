package fdi.ucm.dendro;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndex extends DState {

	protected DState Inicial;
	
	public DendrogramIndex(DCollection collection) {
		Inicial	= new DState();
		for (Integer doc : collection.getResources()) {
			addResource(doc,collection.getTagsFor(doc));
		}
	}

	public void addResource(Integer doc, RoaringBitmap tagsFor) {
		LinkedList<Integer> Lista=new LinkedList<Integer>(); 
		for (Integer integer : tagsFor) {
			Lista.add(integer);
		}
		Collections.sort(Lista);
		
		
		DState Final = Inicial;
		boolean found_=false;
		while (!found_)
		{
			
			List<Integer> IntentNuevo= new LinkedList<Integer>();
			DState Destino=null;
			Integer DestinoI=null;
			boolean newDestiny_=false;
			
			for (Integer integer : Lista) 
				if (Final.getIntent().contains(integer))
					IntentNuevo.add(integer);
				else	
					if (Destino==null&&Final.getTransit().containsKey(integer))
						{
						newDestiny_=true;
						Destino=Final.getTransit().get(integer);
						DestinoI=integer;
						}
				
				
			
			//CASO SOY IGUAL
			if (IntentNuevo.size()==Final.getIntent().getCardinality())
				if (newDestiny_)
					{
					//SOY YO
					found_=true;
					}
				else
				{
					
					if (Destino!=null)
						//Transita
						Final=Destino;
					else
					{
						//Hijo nuevo
						Final=creaNodo(Final,DestinoI,tagsFor);
						found_=true;
					}
					
					
				}
			else
				{
				//Divide y venceras
				Integer Candidato=null;
				for (Integer integer : Final.getIntent()) {
					if (!IntentNuevo.contains(integer))
						{
						Candidato=integer;
						break;
						}
					DState NewStat=Final.cloneS();
					Final.getResources().clear();
					Final.getTransit().clear();
				}
				}
		}
		
		Final.getResources().add(doc);
		
	}

	private DState creaNodo(DState final1, Integer integer,
			RoaringBitmap tagsFor) {
		DState nuevo=new DState();
		nuevo.setIntent(tagsFor);
		final1.getTransit().put(integer, nuevo);
		return nuevo;
	}

	public DendrogramIndex() {
		Inicial	= new DState();
	}

}
