package com.example.filebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 	implements OnItemClickListener,OnClickListener{
	private TextView info;
	private ListView fileList;
	private List<String> items = null;
	private FileScan fs;
	List<Map<String,Object>> fileListMap=null;
	List<String> uploadFileListMap=null;
	private String curPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fs=new FileScan();
		
		info=(TextView)findViewById(R.id.info);
		fileList=(ListView)findViewById(R.id.file);
		
		Button upload=(Button)findViewById(R.id.upload);
		upload.setOnClickListener(this);
		
		Button cancel=(Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		 uploadFileListMap=new ArrayList<String>();
		
		if(!isExternalStorageAvailable())
		{
			Toast.makeText(this, "系统没有SD", 2000).show();
			this.finish();
		}
		String sdInfo="SD："+getTotalExternalMemorySize()+"G剩余："+getAvailableExternalMemorySize()+"G"+"已用："+(double)(Math.round((getTotalExternalMemorySize()-getAvailableExternalMemorySize())*100)/100.00)+"G";
		this.info.setText(sdInfo);
		
		File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
        curPath=path;
        fileListMap=this.getData(path);
        this.setAdapter(fileListMap);
        fileList.setOnItemClickListener(this);
	}
	public void setAdapter(List<Map<String,Object>> fileListMap)
	{
		SimpleAdapter adapter = new SimpleAdapter(this,fileListMap,R.layout.item,new String[]{"img_pre", "text", "detail","isSelected","img_post"},new int[]{R.id.img_pre, R.id.text, R.id.detail,R.id.ans_item_select,R.id.img_post}){
        	@Override  
        	   public View getView(final int position, View convertView, ViewGroup parent)
               {
            	   View view = super.getView(position, convertView, parent);
            	   @SuppressWarnings("unchecked")
				final HashMap<String,Object> map = (HashMap<String, Object>) this.getItem(position);  
                   //获取相应View中的Checkbox对象  
                   CheckBox checkBox = (CheckBox)view.findViewById(R.id.ans_item_select);  
                   checkBox.setChecked((Boolean) map.get("isSelected"));  
                   checkBox.setOnClickListener(new View.OnClickListener() {  
                       @Override  
                       public void onClick(View view) {  
                           map.put("isSelected", ((CheckBox)view).isChecked());  
                           if(((CheckBox)view).isChecked()){  
                               Toast.makeText(MainActivity.this,"选中了"+ map.get("text"), 0).show();  
                               uploadFileListMap.add((String)map.get("path"));
                           }else{  
                        	   Toast.makeText(MainActivity.this,"取消了"+ map.get("text"), 0).show();
                        	   uploadFileListMap.remove((String)map.get("path"));
                           }  
                             
                       }  
                   });  
            	   return view;
               }
        };
        fileList.setAdapter(adapter);
	}
	
	@SuppressWarnings("deprecation")
	public List<Map<String, Object>> getData(String path) { 
    	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    	String sdStateString=Environment.getExternalStorageState();
    	if(sdStateString.equals(Environment.MEDIA_MOUNTED))
    	{
    		try{
    			File sdPath=new File(path);
    			if(sdPath.listFiles().length>0)
    			{
    				for(File file:sdPath.listFiles())
    				{
    					
	    					Map<String,Object> map=new HashMap<String,Object>();
	    					if(file.isDirectory())
	    					{
	    						map.put("img_pre",R.drawable.dir);
	    						map.put("dir", "yes");
	    					}
	    					else
	    					{
	    						map.put("dir", "no");
	    						map.put("img_pre",R.drawable.file);
	    					}
	    					map.put("isSelected", false);
	    					map.put("text", file.getName());
	    					String pro = "";
	    					Date filetime = new Date(file.lastModified());
	    					pro+=filetime.toLocaleString();
	    					if(file.canRead())
	    					{
	    						pro+="r";
	    					}
	    					if(file.canWrite())
	    					{
	    						pro+="w";
	    					}
	    					if(file.canExecute())
	    					{
	    						pro+="X";
	    					}
	    					map.put("detail", pro);
	    					map.put("img_post",R.drawable.ok);
	    					map.put("path", file.getAbsolutePath());
	    					list.add(map);
	    			}
    				uploadFileListMap.clear();
    			}
    			
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		
    	}
    	return list;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//判断 SDCard 是否存在,并且是否具有可读写权限
	public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
	
    
    /**
     * 获取手机外部可用空间大小
     * @return
     */
    static public double getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();//获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (double)(Math.round((availableBlocks * blockSize/1024/1024/1024.0)*100)/100.0);
        } else {
            return -1;
        }
    }

    /**
     * 获取手机外部总空间大小
     * @return
     */
    static public double getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory(); //获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return (double)(Math.round((totalBlocks * blockSize/1024/1024/1024.0)*100)/100.0);
        } else {
            return -1;
        }
    }

	@Override
	public void onItemClick(AdapterView<?> adater, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		Map<String,Object> map=fileListMap.get(position);
		Toast.makeText(this, map.get("path").toString(), 1000).show();
		if(map.get("dir").equals("yes"))
		{
			String path=map.get("path").toString();
			curPath=path;
			fileListMap=this.getData(path);
			this.setAdapter(fileListMap);
		}
	}


	
	private void exitDialog()
	{
		new AlertDialog.Builder(this).setIcon(R.drawable.ok).setTitle("程序退出？").setMessage("您确定退出本程序？").setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		
		}).show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath();
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(!path.equals(curPath))
			{
				File f=new File(curPath);
				curPath=f.getParent().toString();
				fileListMap=this.getData(curPath);
				this.setAdapter(fileListMap);
		        return false;
			}
			else
			{
				exitDialog();
			}
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.upload:
			String info="";
			for(int i=0;i<uploadFileListMap.size();i++)
			{
				info+=uploadFileListMap.get(i).toString()+"\n";
			}
			Toast.makeText(this, info, 1000).show();
			break;
		case R.id.cancel:
			
			break;
		default:
			break;
		}
	}
}
