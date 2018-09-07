package fdi.ucm.dendro;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import org.roaringbitmap.RoaringBitmap;


public class claseInicio {

	private static final Random RAND=new Random();
	private final static int STEP=100;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		 DCollection col = new DCollection();
		   col.load(new FileReader(args[0]));
		   System.out.println("Collection loaded");
		   NavigationAction[] actions = makeActions(col);
		   System.gc();System.gc();System.gc();System.gc();
		   System.out.println("BASIC");
		   simulate(new BasicNavigationSystem(new DCollection(),true),actions,true);
		   System.gc();System.gc();System.gc();System.gc();
		   System.out.println("Dendrogram");
		   simulate(new DendroNavigationSystem(new DCollection(),true),actions,true);
		   
		   
	}
	
	private static NavigationAction[] makeActions(DCollection colIni) {
		 DCollection col = new DCollection();
		 List<Integer> RecursosInsertados=new LinkedList<Integer>();
		 List<Integer> activeTags = new LinkedList<>();
	     List<NavigationAction> actions = new LinkedList<NavigationAction>();

	     
// FAS1
//	     NavigationSystem ns=null;
	     
	     Random r = new Random();
	     int ActualInsertIndex=0;
	     PriorityQueue<Integer> pendientesInsert=new PriorityQueue<Integer>();
	     for (Integer integer : colIni.getResources())
	    	 pendientesInsert.add(integer);
	     
	     while (!pendientesInsert.isEmpty()) {

//FAS1
////	    	 //Borrar un 10
//	    	 if (col.getResources().getCardinality()!=0)
//	    	 {
//	    		 long nactionDel = 10;
//	    		 while (nactionDel>0)
//	    		 {
//	    		 int Borrar=RAND.nextInt(RecursosInsertados.size());//RAND.nextInt(ActualInsertIndex);
//	    		 
//	    		 int Recurso = RecursosInsertados.get(Borrar);
//	                 RecursosInsertados.remove(Borrar);
//	    		 actions.add(makeDeleteAction(Recurso));
//	    		 col.removeObject(Recurso, colIni.getTagsFor(Recurso));
//	    		 nactionDel--;
//	    		 pendientesInsert.add(Recurso);
//	    		 }
//	    	 }
	    	 
	    	 
	    	 //Insercion de 100 Elementos
	    	 
	    	 for (int i = ActualInsertIndex; i < ActualInsertIndex+100 &&!pendientesInsert.isEmpty(); i++) {
	    		 Integer Recurso = pendientesInsert.remove();
	    		 actions.add(makeInsertAction(colIni.getTagsFor(Recurso),Recurso));
	    		 col.addObject(Recurso, colIni.getTagsFor(Recurso));
	    		 col.addTags(colIni.getTagsFor(Recurso));
	    		 RecursosInsertados.add(Recurso);
			}
	    	 
	    	 //Veo cuantos tengo
	    	 ActualInsertIndex=col.getResources().getCardinality();
	    	 
//FAS1	    	 
//	    	 
//	    	 //Lo creo y que construya todo
//	    	 ns = new CachedNavigationSystem(col,false);
//		     ns.init();
//	    	 
//	    	
//	    	 
//	    	 //Calculo acciones
//	    	 long naction = Math.round(ActualInsertIndex*5);
//	    	 
//	    	 while (naction>0)
//	    	 {
//	    		 NavigationAction Aplicar=null; 
//	    		 
//		        if (ns.getActiveTags().getCardinality() > 0 && ns.getSelectableTags().getCardinality() > 0) {
//		            if (RAND.nextInt(2)==0)
//		            	Aplicar = makeAddAction(ns,activeTags);
//		            else 
//		            	Aplicar = makeRemoveAction(ns,activeTags);
//		        }  
//		        else if (ns.getActiveTags().getCardinality() == 0) {
//		        	Aplicar = makeAddAction(ns,activeTags);            
//		        }
//		        else {
//		        	Aplicar = makeRemoveAction(ns,activeTags);
//		        }
//		        ns.run(Aplicar);
//		       naction--;
//		       actions.add(Aplicar);
//	    	 }
	    	 
	    	
	    	 
	    	 
	     }
	     NavigationAction[] Salida=new NavigationAction[actions.size()];
	     for (int i = 0; i < actions.size(); i++)
	    	 Salida[i]=actions.get(i);

	     return Salida;
	 }   
	
	
	 private static NavigationAction makeInsertAction(RoaringBitmap tagsFor, Integer recurso) {
			return new InsertNA(tagsFor,recurso);
		}
	 
	 
	 private static void simulate(NavigationSystem ns,NavigationAction[] actions, boolean out) {
	     long time=0;
	     long begin = System.nanoTime();
	     ns.init();
	     long end = System.nanoTime();
	     time += (end-begin);
	     if (out) System.out.println(0+"\t"+time);
	     for(int a=0; a < actions.length; a++) {
	         begin = System.nanoTime();
	         ns.run(actions[a]);
	         end = System.nanoTime();
	         time += (end-begin);
	         if ((a+1)%STEP == 0 && out) System.out.println((a+1)+"\t"+time);
	     }
	 }
}
