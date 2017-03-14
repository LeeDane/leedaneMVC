package com.cn.leedane.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * 网络相关的工具类
 * @author LeeDane
 * 2016年7月12日 上午10:29:03
 * Version 1.0
 */
public class HttpUtil{

	public HttpUtil() {
	}
	
	/**
	 * 将HttpServletRequest对象转成jSONObject格式的数据
	 * @param request  http请求
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJsonObjectFromInputStream(HttpServletRequest request) throws Exception {
			return getJsonObjectFromInputStream(null,request);
	}
	
	/**
	 * 将HttpServletRequest对象转成jSONObject格式的数据
	 * @param result 结果
	 * @param request  http请求
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJsonObjectFromInputStream(String result,HttpServletRequest request) throws Exception {
		if(result == null){
			InputStream inp = request.getInputStream();
			return JsonUtil.getInstance().toJsonObject(inp);
		}else
			return JSONObject.fromObject(result); 
	}
	/**
	 * Servlet请求转发
	 * @param request http请求
	 * @param response   http响应
	 * @param url  跳转的url
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void dispatch(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException{
		request.getRequestDispatcher(url).forward(request, response);
	}
	
	/**
	 * 输出json数据
	 * @param response  http响应
	 * @param json json对象
	 */
	public static void sendJson(HttpServletResponse response,Object json){
		PrintWriter out=null;
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/x-json");
			out = response.getWriter(); 		
			out.print(json.toString());				
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
	/**
	 * 获取网络图片的InputStream流
	 * @param imgUrl
	 * @return
	 */
	public static InputStream getInputStream(String imgUrl){
		if(StringUtil.isNull(imgUrl))
			return null;
		
		URL url = null;
		try {
			url = new URL(imgUrl);
			URLConnection uc = url.openConnection(); 
			uc.setConnectTimeout(60000);
			uc.setDoInput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(30000);
			return uc.getInputStream(); 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**  
     * 向指定URL发送POST方法的请求  
     * @param url 发送请求的URL  
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。  
     * @return URL所代表远程资源的响应  
     */  
    public static String sendPost(String url, String param) {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try {  
            URL realUrl = new URL(url);  
            //打开和URL之间的连接  
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();  
            //设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent",  
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
            //conn.setRequestProperty("Accept-Charset", "UTF-8");
            //发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setRequestMethod("POST");
            //获取URLConnection对象对应的输出流  
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()));  
            param = URLEncoder.encode(param, "GBK");
            //发送请求参数  
            out.print(param);
            //flush输出流的缓冲  
            out.flush();  
            //定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                new InputStreamReader(conn.getInputStream()));  
            String line;  
            while ((line = in .readLine()) != null) {  
                result += "/n" + line;  
            }  
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }  
        //使用finally块来关闭输出流、输入流  
        finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if ( in != null) { in .close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    } 
}
