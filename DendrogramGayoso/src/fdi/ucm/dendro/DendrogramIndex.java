package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.roaringbitmap.RoaringBitmap;

public class DendrogramIndex {

	protected DState Inicial;
	protected HashMap<Integer, DState> TablaRecursos;
	
	public DendrogramIndex() {
		Inicial	= new DState();
		TablaRecursos= new HashMap<Integer, DState>();
	}
	
	public DendrogramIndex(DCollection collection) {
		Inicial	= new DState();
		TablaRecursos= new HashMap<Integer, DState>();
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
				if (!processT.contains(integer))
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
						Final=creaNodo(Final, processT);
						found_=true;
						}
				}
				else
				{
//					Divido
					DState NewStat=Final.cloneS(toExtend);
					Final.getResources().clear();
					Final.getIntent().clear();
					for (Integer integer2 : newIntent) 
						Final.getIntent().add(integer2);
					for (Integer integer2 : toExtend)
						Final.getExtend().add(integer2);
					
					Final.getTransit().add(NewStat);
					NewStat.setFather(Final);
					
					Final=creaNodo(Final, processT);
					found_=true;
					
					
				}
					
			
			
			}
			
			
			
			
			
		}
		Final.getResources().add(doc);
		TablaRecursos.put(doc, Final);
		
	}

	private DState getDestino(DState final1, Integer integer) {
		for (DState stateB : final1.getTransit()) {
			if (stateB.getIntent().contains(integer))
				return stateB;
		}
		return null;
	}

	private DState creaNodo(DState final1, Set<Integer> processT) {
		DState nuevo=new DState();
		for (Integer integer : processT)
			nuevo.getIntent().add(integer);
		final1.getTransit().add(nuevo);
		nuevo.setFather(final1);
		return nuevo;
	}

	

	public void DeleteResource(int resource, RoaringBitmap tagsFor,
			DCollection collection) {
		DState Final = Inicial;
		DState Father = null;
		boolean found_=false;
		
		Set<Integer> processT= new HashSet<Integer>();
		for (Integer integer : tagsFor)
			processT.add(integer);
		
		
		DState SalidaN=TablaRecursos.get(new Integer(resource));
		
		if (SalidaN!=null)
		{
		
		Final= SalidaN;
		Father= SalidaN.getFather();
		
		while (!found_)
		{
			if (Final.getResources().contains(resource))
			{
				found_=true;
				break;
			}
			
			for (Integer integer : Final.getIntent())
				processT.remove(integer);
			
			//DIFICIL
		}
		
		if (found_)
		{
			Final.getResources().remove(resource);
			
			if (Final.getResources().getCardinality()==0&&Final.getTransit().isEmpty())
			{
				//Este es candidato a ser borrado
				if (Father!=null)
					{
					Father.getTransit().remove(Final);
					
					if (Father.getTransit().size()==1)
					{
						
						DState Hijo = Father.getTransit().remove(0);
						for (Integer hijo : Hijo.getResources())
							Father.getResources().add(hijo);

						for (Integer hijo : Hijo.getIntent())
							{
							Father.getIntent().add(hijo);
							Father.getExtend().remove(hijo);
							}
						
						
					}
					}
				
			}
			
		}
		}
		
		
		
	}




}
