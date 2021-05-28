package Filemanger;
import Tool.Hash;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileManager {

    private long userID;
    private ArrayList<FileInfo> fileList=new ArrayList<FileInfo>();
    public byte[] FileToBytes(String trueName){
    String filePath=userID+trueName;
    File file1 = new File(filePath);
    byte[] bytes = File2byte(file1);

    //文件发送
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    String time=df.format(new Date());// new Date()为获取当前系统时间

    for(int i = 0;i < fileList.size(); i++){   //更新下载时间
        //System.out.println(fileList.get(i).trueName+"       "+trueName);
        if(fileList.get(i).trueName.equals(trueName)){
            //System.err.println("!!!!!");
            FileInfo fileInfo = new FileInfo();
            fileInfo.trueName=fileList.get(i).trueName;
            fileInfo.storeName=fileList.get(i).storeName;
            fileInfo.Modification=fileList.get(i).Modification+100;
            fileInfo.downTime= time;
            fileInfo.fileSize= fileList.get(i).fileSize;
            fileInfo.upTime=fileList.get(i).upTime;
            fileList.set(i,fileInfo);

            break;
        }
    }
    return bytes;
}
    public void BytesToFile(byte[] bytes,String truename){
    String filepath=userID+truename;
    getFile(bytes,"E:\\2021年度上文件\\学习\\姚宏\\总课设\\日志类",filepath);

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    String time=df.format(new Date());// new Date()为获取当前系统时间
    FileInfo fileInfo = new FileInfo();
    fileInfo.trueName=truename;
    fileInfo.storeName=filepath;
    fileInfo.Modification=001;
    fileInfo.downTime= String.valueOf(0);
    fileInfo.fileSize= bytes.length;
    fileInfo.upTime=time;
    fileList.add(fileInfo);


}
    public long getFileSize(String truename){
        for(int i = 0;i < fileList.size(); i++){   //更新下载时间
            //System.out.println(fileList.get(i).trueName+"       "+trueName);
            if(fileList.get(i).trueName.equals(truename)) {
                //System.err.println("!!!!!");

                return fileList.get(i).fileSize;
                }
            }
        return 0;
    }
    public byte[] HashFile(String trueName){
        String filePath=userID+trueName;
        File file1 = new File(filePath);
        System.out.println(file1);
        byte[] bytes = File2byte(file1);
        return  Hash.md5(bytes);

    }
    private static FileManager instance;
    public void setuid(long uid){//对外接口 ，设置用户名
        this.userID= uid;
    }
    public static synchronized FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        System.out.println("在位置"+filePath+"生成"+fileName);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
    public void updateFileList(){//上传更新数据库

        for(int i = 0;i < fileList.size(); i ++){
            int mod=fileList.get(i).Modification;
            String sql = null;
           //if(mod==000){}//未修改
             if(mod==001){
                 sql="insert into FileInfo(uid,fileSize, upTime, downTime,trueName,storeName ) values('";
                 sql=sql+userID+"','";
                 sql=sql+fileList.get(i).fileSize+"','";
                 sql=sql+fileList.get(i).upTime+"','";
                 sql=sql+fileList.get(i).downTime+"','";
                 sql=sql+fileList.get(i).trueName+"','";
                 sql=sql+fileList.get(i).storeName+"'";
                 sql=sql+")";
                 System.err.println(sql);

             }//新文件
            else if(mod==100){
                 sql="update FileInfo\n" +
                         "\n" +
                         "set downTime='"+fileList.get(i).downTime+"'\n" +
                         "\n" +
                         "where trueName='"+fileList.get(i).trueName+"'";
                 System.err.println(sql);

             }//修改下载时间
            else if(mod==010){
                 sql="update FileInfo\n" +
                         "\n" +
                         "set trueName='"+fileList.get(i).trueName+"'\n" +
                         "\n" +
                         "where storeName='"+fileList.get(i).storeName+"'";
                 System.err.println(sql);
             }//修改文件名
            else if(mod==110){
                 sql="update FileInfo\n" +
                         "\n" +
                         "set downTime='"+fileList.get(i).downTime+"'," +"trueName='"+fileList.get(i).trueName+"'\n"+
                 "\n" +
                         "where storeName='"+fileList.get(i).storeName+"'";
                 System.err.println(sql);
             }//都修改
           if(sql!=null) {DateBaseLink.getInstance().execute(sql);
            LogManager.getInstance().writeLog(sql);}
        }


        /*for(int i=0;i<6;i++)
        {
            sql=sql+fileList.get(i).fileNumber+","
        }*/

    }
    public void sync(){//从数据库同步
        String sql="SELECT fileSize, upTime, downTime,trueName,storeName FROM FileInfo where uid="+userID;
        //FileInfo newfileList = new FileInfo();
        ArrayList alRowData =DateBaseLink.getInstance().execute(sql);


        for(int i = 0;i < alRowData.size(); i ++){ //格式转换
            //System.out.println(i);
            //System.out.println("++++++++++++++++++++++");
            FileInfo newfileList = new FileInfo();
            newfileList.trueName= (String) alRowData.get(i+3);
            newfileList.storeName= (String) alRowData.get(i+4);
            newfileList.Modification=000;
            newfileList.downTime= (String) alRowData.get(i+2);
            newfileList.fileSize= (int) alRowData.get(i);
            newfileList.upTime= (String) alRowData.get(i+1);
            String filelist =newfileList.trueName+" "+newfileList.storeName+" "+newfileList.Modification+" "+newfileList.downTime+" "+newfileList.fileSize+" "+newfileList.upTime;
            System.out.println(filelist);
            fileList.add(newfileList);
            LogManager.getInstance().writeLog(filelist);
            i=i+4;
           // System.err.println(i);
           // System.out.println("++++++++++++++++++++++");
            //fileList.add((FileInfo) alRowData.get(i));

        }

        /*for(int i = 0;i < fileList.size(); i ++){
            String filelist = null;
            filelist=fileList.get(i).fileSize+fileList.get(i).upTime+fileList.get(i).downTime+fileList.get(i).storeName+fileList.get(i).trueName;
            System.out.println(filelist);
            logManager.writeLog(filelist);
            fileList.get(i).Modification=000;
        }*/

    }
    public ArrayList<FileInfo> getFileList(){
        return  fileList;
    }
    public void renameFile(String newName,String oldName){

       //String path = "E:/MINE/senior/face/";

        for(int i = 0;i < fileList.size(); i ++){   //更新下载时间
            if(fileList.get(i).trueName==oldName){
                fileList.get(i).trueName=newName;
                fileList.get(i).Modification+=010;
                break;
            }
        }
        /**修改真实文件名
        File file;
        file=new File(oldName); //指定文件名及路径

        if (file.renameTo(new File(newName))) {
            System.out.println("修改成功!");
        }
        else{
            System.out.println("修改失败");
        }
*/

    }
}
/*
-sync():void
-updateFileList():void
+getFileList():ArrayList<FileInfo>
+uploadFile(FileInfo fileInfo):void
+downloadFile(String trueName):String
+renameFile(String newName,String oldName):void
 */