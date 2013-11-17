package com.example.filebrowser;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleAdapter;

public class MyAdapter extends SimpleAdapter {

	
	//List<Map<String,Object>> fileListMap
	public MyAdapter(Context context, List<Map<String,Object>> fileListMap,int layout, String[] from, int[] to) {
		super(context, fileListMap, layout, from, to);
	}
}  