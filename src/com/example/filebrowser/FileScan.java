package com.example.filebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

public  class FileScan {
    
    private static final String TAG = "FileScan";
    public HashMap<String, String> getListOnSys(File file) {
        
        //从根目录开始扫描
        Log.i(TAG, file.getPath());
        HashMap<String, String> fileList = new HashMap<String, String>();
        getFileList(file, fileList);
        return fileList;
    }
    
    /**
     * @param path
     * @param fileList
     * 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    private void getFileList(File path, HashMap<String, String> fileList){
        //如果是文件夹的话
        if(path.isDirectory()){
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if(null == files)
                return;
            
            for(int i = 0; i < files.length; i++){
                getFileList(files[i], fileList);
            }
        }
        //如果是文件的话直接加入
        else{
            Log.i(TAG, path.getAbsolutePath());
            //进行文件的处理
            String filePath = path.getAbsolutePath();
            //文件名
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            //添加
            fileList.put(fileName, filePath);
        }
    }
    
    
    
    
   
}