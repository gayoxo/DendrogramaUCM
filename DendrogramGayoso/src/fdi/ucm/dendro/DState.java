package fdi.ucm.dendro;

import java.util.LinkedList;
import java.util.List;

import org.roaringbitmap.RoaringBitmap;

public class DState {

	 private RoaringBitmap resources;
	 private RoaringBitmap intent;
	 private RoaringBitmap extend;
	 private List<DState> transit;
	 private DState father;
	 private Integer Ide;
	 
	 
	 public DState(int ide) {
		Ide=new Integer(ide);
		resources=new RoaringBitmap();
		intent=new RoaringBitmap();
		extend=new RoaringBitmap();
		transit=new LinkedList<DState>();
		father=null;
	}
	
	public RoaringBitmap getResources() {
		return resources;
	}
	
	public RoaringBitmap getIntent() {
		return intent;
	}
	
	public List<DState> getTransit() {
		return transit;
	}
	
	public void setIntent(RoaringBitmap intent) {
		this.intent = intent;
	}

	public RoaringBitmap getExtend() {
		return extend;
	}
	
	public DState cloneS(Integer ide) {
		DState Salida=new DState(ide);
		Salida.getIntent().or(intent);
		Salida.getResources().or(resources);
		
//		for (Integer integer : intent)
//			Salida.getIntent().add(integer);
//		
//		for (Integer integer : resources) 
//			Salida.getResources().add(integer);

		
		return Salida;
	}
	
	public DState getFather() {
		return father;
	}
	
	public void setFather(DState father) {
		this.father = father;
	}

	public Integer getIde() {
		return Ide;
	}

	public DState cloneS(List<Integer> toExtend, int ide) {
		DState Salida=new DState(ide);
		for (Integer integer : toExtend)
			Salida.getIntent().add(integer);
		
		for (Integer integer : resources) 
			Salida.getResources().add(integer);

		
		return Salida;
	}
}
