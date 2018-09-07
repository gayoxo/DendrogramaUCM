package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.roaringbitmap.RoaringBitmap;

public class DState {

	 private RoaringBitmap resources;
	 private RoaringBitmap intent;
	 private RoaringBitmap extend;
	 private List<DState> transit;
	 
	 
	 public DState() {
		resources=new RoaringBitmap();
		intent=new RoaringBitmap();
		extend=new RoaringBitmap();
		transit=new LinkedList<DState>();
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
	
	public DState cloneS() {
		DState Salida=new DState();
		for (Integer integer : intent)
			Salida.getIntent().add(integer);
		
		for (Integer integer : resources) 
			Salida.getResources().add(integer);

		
		return Salida;
	}
	
	
}
