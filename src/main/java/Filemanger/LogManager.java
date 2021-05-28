package Filemanger;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private String filename;

    private static LogManager instance;
    public static synchronized LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }
    public void setuid(String uid){//对外接口 ，设置用户名
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time=df.format(new Date());// new Date()为获取当前系统时间
        filename= uid+" "+time;

    }

    public int writeLog(String content){
        int length=content.length();
        try {
            String file=filename+".txt";
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(file, true);//尾部追加
            //BufferedWriter writer = new BufferedWriter(new FileWriter(file));//重写
            writer.write(content);
            writer.write("\n");
            writer.close();
            System.out.println("日志：" +
                    ""+filename+"写入成功！");
        } catch (IOException e) {
            e.printStackTrace();}


       return  length;
    }
}
