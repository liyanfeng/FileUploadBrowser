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
        
        //�Ӹ�Ŀ¼��ʼɨ��
        Log.i(TAG, file.getPath());
        HashMap<String, String> fileList = new HashMap<String, String>();
        getFileList(file, fileList);
        return fileList;
    }
    
    /**
     * @param path
     * @param fileList
     * ע����ǲ��������е��ļ��ж����Խ��ж�ȡ�ģ�Ȩ������
     */
    private void getFileList(File path, HashMap<String, String> fileList){
        //������ļ��еĻ�
        if(path.isDirectory()){
            //�����ļ������е�����
            File[] files = path.listFiles();
            //���ж�����û��Ȩ�ޣ����û��Ȩ�޵Ļ����Ͳ�ִ����
            if(null == files)
                return;
            
            for(int i = 0; i < files.length; i++){
                getFileList(files[i], fileList);
            }
        }
        //������ļ��Ļ�ֱ�Ӽ���
        else{
            Log.i(TAG, path.getAbsolutePath());
            //�����ļ��Ĵ���
            String filePath = path.getAbsolutePath();
            //�ļ���
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            //���
            fileList.put(fileName, filePath);
        }
    }
    
    
    
    
   
}