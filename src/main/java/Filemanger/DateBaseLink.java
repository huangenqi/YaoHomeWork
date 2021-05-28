package Filemanger;

import java.sql.*;
import java.util.ArrayList;

public class DateBaseLink {
    static Statement sql;

    private static DateBaseLink instance;
    public static synchronized DateBaseLink getInstance() {
        if (instance == null) {
            instance = new DateBaseLink();
        }
        return instance;
    }
    public void connect(String ip,int port,String dbid,String uid,String pwd){
        try {
            // 加载数据库驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 数据库连接字符串
            // jdbc:sqlserver             连接协议
            // localhost                  连接地址
            // 1433                       连接端口
            // MyDatabase                 连接的数据库

            String url = "jdbc:sqlserver://"+ip+":"+port+";databaseName="+dbid;
            System.out.println(url);
            // 打开数据库连接
            // sa   用户名
            // 1    刚刚设置的密码
            Connection connection = DriverManager.getConnection(url, uid, pwd);
            sql = connection.createStatement();
            // 关闭数据库连接
            //connection.close();

            System.out.println("数据库连接成功");
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }



    }

     public ArrayList execute(String value) {

         ArrayList alRowData = new ArrayList();
         try { // try语句捕捉异常

             boolean gotResults = sql.execute(value);
             ResultSet res = null;
             if(!gotResults){
                 System.out.println("No results returned");
                 return alRowData;
             } else {
                 res = sql.getResultSet();
             }
             /*ResultSet res = null;
             if(sql.executeQuery(value)!=null)
             { res = sql.executeQuery(value);}// 执行SQL语句
             else return alRowData;*/
             while (res.next()) { // 如果当前记录不是结果集中的最后一条，进入循环体


                 ResultSetMetaData rsmd = res.getMetaData();
                 int numberOfColumns = rsmd.getColumnCount();
                 for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++) {
                     alRowData.add( res.getObject(columnIndex));
                 }

                 //System.out.println(alRowData);
             }
         } catch (Exception e) { // 处理异常
             e.printStackTrace(); // 输出异常信息
         }
         return alRowData;
     }
    public ResultSet executeRS(String value) {
        boolean gotResults = false;
        try {
            gotResults = sql.execute(value);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet res = null;
        if(!gotResults){
            System.out.println("No results returned");
            return res;
        } else {
            try {
                res = sql.getResultSet();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return res;
    }

}
