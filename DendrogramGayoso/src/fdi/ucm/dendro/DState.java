package fdi.ucm.dendro;

import java.awt.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.roaringbitmap.RoaringBitmap;

public class DState {

	 private RoaringBitmap resources;
	 private RoaringBitmap intent;
	 private Map<Integer,DState> transit;
	 
	 
	 public DState() {
		resources=new RoaringBitmap();
		intent=new RoaringBitmap();
		transit=new HashMap<>();
	}
	
	public RoaringBitmap getResources() {
		return resources;
	}
	
	public RoaringBitmap getIntent() {
		return intent;
	}
	
	public Map<Integer, DState> getTransit() {
		return transit;
	}
	
	public void setIntent(RoaringBitmap intent) {
		this.intent = intent;
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
