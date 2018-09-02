package fdi.ucm.dendro;

import java.util.HashMap;
import java.util.Map;

import org.roaringbitmap.RoaringBitmap;

public class DState {

	 private RoaringBitmap resources;
	 private RoaringBitmap intent;
	 private Map<Integer,RoaringBitmap> transit;
	 
	 
	 public DState() {
		resources=new RoaringBitmap();
		intent=new RoaringBitmap();
		transit=new HashMap<>();
	}
}
