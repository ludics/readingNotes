package com.crowd.diary.internet;

/**
 * Created by leon on 17-12-12.
 */
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Communicate {
    String myUrl = "http://www.ludics.cn/app/";
    
    public static final int USEREXIST = -1;
    public static final int NOTEXIST = -2;
    public static final int PSWDERR = -3;
    public static final int NOTCONNECT = -404;
    public static final int FAIL = -4;
    public static final int SUCCESS = -5;

    // Logger log = new Logger(this.getClass());//初始化日志类
    /**
     * @作用 使用urlconnection
     * @param username
     * @param password
     * @return 
     * @throws IOException
     */ 
    
    public int registerToServer(String username,String password) throws IOException{
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        int response = -404;
        String re = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(myUrl+"signup.php");
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            String data = "&userName=" + username + "&userPassword=" + password;
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.connect();
            //POST请求
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            //读取响应
            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() != 200){
                response = NOTCONNECT;
            } else{
                reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    re+=lines;
                    // System.out.println(lines);
                }
                reader.close();
                // 断开连接
                conn.disconnect();
            }
            // System.out.println(response.toString());
            // log.info(response.toString());
        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        if (re.contains("existed")) 
            response = USEREXIST;
        else if (re.contains("userID")){
            try{
                JSONObject obj = new JSONObject(re);
                response = obj.getInt("userID");
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return response;
    }
    /**
     * @作用 登录
     * @param username
     * @param password
     * @return "Username existed, register failed." 
     * @return "Register success."
     * @throws IOException
     */

    public int loginToServer(String username, String password) throws IOException{
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        int response= -404;
        String re = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(myUrl+"user_login.php");
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            String data = "&userName=" + username + "&userPassword=" + password;
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
            conn.connect();
            //POST请求
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            //读取响应
            if (conn.getResponseCode() != 200){
                response = NOTCONNECT;
            } else{
                reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    re+=lines;
                    System.out.println(lines);
                }
                reader.close();
                // 断开连接
                conn.disconnect();
            }
            // System.out.println(response.toString());
            // log.info(response.toString());
        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        if (re.contains("error")) 
            response = PSWDERR;
        else if (re.contains("not"))
            response = NOTEXIST;
        else if (re.contains("userID")){
            try{
                JSONObject obj = new JSONObject(re);
                response = obj.getInt("userID");
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return response;
    }

    // - int addNoteToServer(int userid, String note, String bookname, String text)
    // 参数为用户id，笔记内容，书名，原文内容，
    // 返回为笔记 id 或者 FAIL
    public int addNoteToServer(int userid, String note, String bookname, String text) throws IOException{
        int response = -404;
        String re = "";
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            
            //JSONObject  obj = new JSONObject();
            // Json build
            JSONObject obj = new JSONObject();
            obj.put("userID", userid);
            obj.put("note", note);
            obj.put("bookname", bookname);
            obj.put("text", text);
            
            // 创建url资源
            URL httpUrl = null; //HTTP URL类 用这个类来创建连
            httpUrl = new URL(myUrl+"add_note.php");
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);            
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = (obj.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            // 开始连接请求
            conn.connect();

            OutputStream os = conn.getOutputStream();
            os.write(data);
            os.flush();
            // OutputStream  out = conn.getOutputStream();     
            // 写入请求的字符串
            // out.write((obj.toString()).getBytes());
            // out.flush();
            // out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (conn.getResponseCode() != 200){
                response = NOTCONNECT;
            } else{
                reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    // System.out.println(lines);
                    re+=lines;
                }
                reader.close();
                // 断开连接
                conn.disconnect();
            }

        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        if (re.contains("noteID")){
            try{
                JSONObject obj = new JSONObject(re);
                response = obj.getInt("noteID");
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
    public int deleteNoteToServer(int noteID) throws IOException{
        int response = -404;
        String re = "";
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        try {
            //JSONObject  obj = new JSONObject();
            // Json build
            JSONObject obj = new JSONObject();
            obj.put("noteID", noteID);
            
            // 创建url资源
            URL httpUrl = null; //HTTP URL类 用这个类来创建连
            httpUrl = new URL(myUrl+"delete_note.php");
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);            
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = (obj.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            // 开始连接请求
            conn.connect();

            OutputStream os = conn.getOutputStream();
            os.write(data);
            os.flush();
            // OutputStream  out = conn.getOutputStream();     
            // 写入请求的字符串
            // out.write((obj.toString()).getBytes());
            // out.flush();
            // out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (conn.getResponseCode() != 200){
                response = NOTCONNECT;
            } else{
                reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    // System.out.println(lines);
                    re+=lines;
                }
                reader.close();
                // 断开连接
                conn.disconnect();
            }

        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        if (re.contains("success")){
            response = SUCCESS;
        } else if (re.contains("fail")){
            response = FAIL;
        }
        return response;
    }
}
