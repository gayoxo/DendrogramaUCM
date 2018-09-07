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
					
					Final=creaNodo(Final, processT);
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

	private DState creaNodo(DState final1, Set<Integer> processT) {
		DState nuevo=new DState();
		for (Integer integer : processT)
			nuevo.getIntent().add(integer);
		final1.getTransit().add(nuevo);
		return nuevo;
	}

	public DendrogramIndex() {
		Inicial	= new DState();
	}

	public void DeleteResource(int resource, RoaringBitmap tagsFor,
			DCollection collection) {
		DState Final = Inicial;
		DState Father = null;
		boolean found_=false;
		
		Set<Integer> processT= new HashSet<Integer>();
		for (Integer integer : tagsFor)
			processT.add(integer);
		
		
		DState[] Salida=FindResourceDelete(tagsFor,resource);
		
		Final= Salida[0];
		Father= Salida[1];
		
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

	private DState[] FindResourceDelete(RoaringBitmap tagsFor, int resource) {
		DState[] Salida=new DState[2];
		
		if (Inicial.getResources().contains(resource))
			Salida[0]=Inicial;
		else
			Salida=recursiveFind(tagsFor,resource,Inicial);
		
		return Salida;
	}

	private DState[] recursiveFind(RoaringBitmap tagsFor, int resource,
			DState father) {
		DState[] Salida=new DState[2];
		
		LinkedList<DState> posibles=new LinkedList<DState>();
		
		for (DState integer : father.getTransit()) {
			for (Integer integerR : tagsFor) {
				if (integer.getIntent().contains(integerR))
					{
					posibles.add(integer);
					break;
					}
			}
			
		}
		
		for (DState dState : posibles) {
			if (dState.getResources().contains(resource))
				{
				Salida[0]=dState;
				Salida[1]=father;
				}
			else
				Salida=recursiveFind(tagsFor,resource,dState);
			
			if (Salida[0]!=null&&Salida[1]!=null)
				break;
		}
		
		return Salida;
	}

}
