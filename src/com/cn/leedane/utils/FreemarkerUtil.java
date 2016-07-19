package com.cn.leedane.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemarker模板解析工具类
 * @author LeeDane
 * 2016年7月12日 上午10:28:05
 * Version 1.0
 */
public class FreemarkerUtil {
	/**
	 * 默认保存的freemarker文件夹的路径
	 */
	public final static String DEFAULT_FREEMARKER_FOLDER = "src/freemarker";
	
	/**
	 * 获得freemarker模板对象
	 * @param templateName  模板的名称
	 * @return
	 */
	public static Template getTemplate(String templateName){
		return getTemplate(FreemarkerUtil.DEFAULT_FREEMARKER_FOLDER,templateName);
	}
	
	/**
	 * 获得freemarker模板对象
	 * @param folder  freemarker文件夹的路径
	 * @param templateName  模板的名称
	 * @return
	 */
	public static Template getTemplate(String folder, String templateName){
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(folder));
			return cfg.getTemplate(templateName);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * 输出文件位置的writer对象
	 * @param outFileName  文件的名称
	 * @return
	 */
	public static Writer getWriter(String outFileName){
		try {
			return new FileWriter(new File(outFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 渲染freemarker模板,并输出模板的字符串
	 * @param rootMap  填充模板的数据
	 * @param templateName  模板的名称
	 * @param outFileName
	 * @param isSave
	 * @return
	 */
	public static String readerTemplate(Object rootMap, String templateName){
		//临时保存加载的模板文件，会自动删除
		String outFileName = "test"+System.currentTimeMillis()+".txt"; 
		StringBuffer buffer = new StringBuffer();
		Template template = getTemplate(templateName);
		Writer out = getWriter(outFileName);
		try {
			template.process(rootMap, out);
			out.flush();
			File file = new File(outFileName);
			InputStreamReader ireader = new InputStreamReader(new FileInputStream(file));
			//3.为了达到最高效率，可要考虑在 BufferedReader内包装 InputStreamReader
			BufferedReader bufr = new BufferedReader(ireader);
			BufferedWriter bufw = new BufferedWriter(out);
			
			int ch = 0;
			// 以字符方式显示文件内容 
			while((ch = bufr.read()) != -1) {
			   buffer.append((char)ch);
			}

			if(bufr != null) bufr.close();
			if(bufw != null) bufw.close();
			
			//删除掉outFileName
			file.deleteOnExit();
		   
			return buffer.toString();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 渲染freemarker模板
	 * @param rootMap  填充模板的数据
	 * @param templateName  模板的名称
	 * @param outFileName
	 * @param isSave
	 * @return
	 */
	public static void readerTemplate(Object rootMap, String templateName,String outFileName){
		StringBuffer buffer = new StringBuffer();
		Template template = getTemplate(templateName);
		Writer out = getWriter(outFileName);
		try {
			template.process(rootMap, out);
			out.flush();
			File file = new File(outFileName);
			InputStreamReader ireader = new InputStreamReader(new FileInputStream(file));
			//3.为了达到最高效率，可要考虑在 BufferedReader内包装 InputStreamReader
			BufferedReader bufr = new BufferedReader(ireader);
			BufferedWriter bufw = new BufferedWriter(out);
			
			int ch = 0;
			// 以字符方式显示文件内容 
			while((ch = bufr.read()) != -1) {
			   buffer.append((char)ch);
			}

			if(bufr != null) bufr.close();
			if(bufw != null) bufw.close();
			
		   
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userName", "LeeDane");
		System.out.println(readerTemplate(map,"Test.ftl"));
	}

}
