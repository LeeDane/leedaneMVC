package com.cn.leedane.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.leedane.model.FilePathBean;
import com.cn.leedane.model.UploadBean;
import com.cn.leedane.model.UserBean;
import com.cn.leedane.service.FilePathService;
import com.cn.leedane.service.UploadService;
import com.cn.leedane.utils.ConstantsUtil;
import com.cn.leedane.utils.DateUtil;
import com.cn.leedane.utils.EnumUtil;
import com.cn.leedane.utils.EnumUtil.DataTableType;
import com.cn.leedane.utils.EnumUtil.ResponseCode;
import com.cn.leedane.utils.FileUtil;
import com.cn.leedane.utils.JsonUtil;
import com.cn.leedane.utils.StringUtil;

@Controller
@RequestMapping("/leedane/filepath")
public class FilePathController extends BaseController{

	protected final Log log = LogFactory.getLog(getClass());

	//上传filePath表的service
	@Autowired
	private FilePathService<FilePathBean> filePathService;
	
	@Autowired
	private UploadService<UploadBean> uploadService;
	

	/**
	 * 分页获取指定的图片列表
	 * @return
	 */
	@RequestMapping("/getUserImagePaging")
	public String getUserImagePaging(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			List<Map<String, Object>> result= filePathService.getUserImageByLimit(getJsonFromMessage(message), getUserFromMessage(message), request);
			System.out.println("获得文件路径的数量：" +result.size());
			message.put("isSuccess", true);
			message.put("message", result);
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}
	
	/**
     * 合并单个文件
     * 对断点上传的文件进行合并
     * @return
     */
	@RequestMapping("/mergePortFile")
    public String mergePortFile(HttpServletRequest request, HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	Map<String, Object> message = new HashMap<String, Object>();
    	long start = System.currentTimeMillis();
        try {
        	if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
        	JSONObject json = getJsonFromMessage(message);
        	UserBean user = getUserFromMessage(message);
        	String fileName = JsonUtil.getStringValue(json, "fileName");//必须
        	String tableUuid = JsonUtil.getStringValue(json, "uuid");  //客户端生成的唯一性uuid标识符
        	String tableName = JsonUtil.getStringValue(json, "tableName");  //客户端生成的唯一性uuid标识符
        	int order = JsonUtil.getIntValue(json, "order", 0); //多张图片时候的图片的位置，必须，为空默认是0	
        	String version = JsonUtil.getStringValue(json, "file_version");  //文件版本信息
        	String desc = JsonUtil.getStringValue(json, "file_desc"); //文件的描述信息
            
        	
        	//只有APP_Version才需要版本号和版本描述信息
        	if(user.isAdmin() && ConstantsUtil.UPLOAD_APP_VERSION_TABLE_NAME.equalsIgnoreCase(tableName)){
				if(StringUtil.isNull(version) || StringUtil.isNull(desc)){
					message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.某些参数为空.value));
					message.put("responseCode", EnumUtil.ResponseCode.某些参数为空.value);
					printWriter(message, response, start);
					return null;
				}
			}
            //filePathService.merge(tableUuid, tableName, order, user, request);
            List<Map<String, Object>> list = uploadService.getOneUpload(tableUuid, tableName, order, user, request);
            if(list != null && list.size() >0){
            	
            	StringBuffer sourcePath = new StringBuffer();
            	sourcePath.append(ConstantsUtil.DEFAULT_SAVE_FILE_FOLDER);
            	sourcePath.append("temporary//");
            	sourcePath.append(user.getId());
            	sourcePath.append("_");
            	sourcePath.append(tableUuid);
            	sourcePath.append("_");
            	sourcePath.append(DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
            	sourcePath.append("_");
            	sourcePath.append(fileName);
            	
            	File sourceFile = new File(sourcePath.toString());
            	if(sourceFile.exists()){
            		sourceFile.deleteOnExit();
            		sourceFile.createNewFile();
            	}else{
            		sourceFile.createNewFile();
            	}
            	
            	//保存合并后的输出对象
            	FileOutputStream out = new FileOutputStream(sourceFile);
            	for(Map<String, Object> map: list){
            		if(!FileUtil.readFile(StringUtil.changeNotNull(map.get("path")), out)){
            			printWriter(message, response, start);
            			return null;
            		}
            	}
            	//合并后关闭流
            	if(out != null){
            		out.flush();
            		out.close();
            	}
            	message.put("isSuccess", filePathService.saveSourceAndEachFile(sourcePath.toString(), user, tableUuid, tableName, order, version, desc));
            	
            	printWriter(message, response, start);
        		return null;
            }else{
            	message.put("message", ResponseCode.没有操作实例.value);
            	message.put("responseCode", EnumUtil.getResponseValue(ResponseCode.没有操作实例.value));
            	printWriter(message, response, start);
        		return null;
            }
        } catch (Exception e) {
            System.out.println("合并文件发生异常,错误原因 : " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("合并文件总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
    }
    
    /**
     * 合并成功后删除片段的临时文件
     * @return
     */
	@RequestMapping("/deletePortFile")
    public String deletePortFile(HttpServletRequest request, HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	Map<String, Object> message = new HashMap<String, Object>();
    	long start = System.currentTimeMillis();
        try {
        	if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
        	JSONObject json = getJsonFromMessage(message);
        	UserBean user = getUserFromMessage(message);
        	String tableUuid = JsonUtil.getStringValue(json, "uuid"); //客户端生成的唯一性uuid标识符
        	String tableName = JsonUtil.getStringValue(json, "tableName");  //客户端生成的唯一性uuid标识符
        	if(tableName.equalsIgnoreCase(DataTableType.用户.value)){
        		System.out.println("更新用户的头像的缓存数据");
        		userHandler.updateUserPicPath(user.getId(), "30x30");
        	}
        	int order = JsonUtil.getIntValue(json, "order", 0); //多张图片时候的图片的位置，必须，为空默认是0	
            List<Map<String, Object>> list = uploadService.getOneUpload(tableUuid, tableName, order, user, request);
            message.put("isSuccess", true);
            
            if(list != null && list.size() >0){   	
            	System.out.println("删除的文件的数量："+list.size());
            	UploadBean upload;
            	for(Map<String, Object> map: list){
        			upload = new UploadBean();
        			upload.setTableName(tableName);
        			upload.setTableUuid(tableUuid);
        			upload.setfOrder(order);
        			upload.setSerialNumber(StringUtil.changeObjectToInt(map.get("serial_number")));
        			upload.setPath(String.valueOf(map.get("path")));
        			if(!uploadService.cancel(upload, user, request)){
        				printWriter(message, response, start);
        				return null;
        			}
            	}
            	message.put("isSuccess", true);
            	printWriter(message, response, start);
        		return null;
            }else{
            	System.out.println("删除的文件的数量为0");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("删除断点片段文件发生异常,错误原因 : " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("删除断点片段文件总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
    }
    
    /**
     * 分页获取上传的文件
     * @return
     */
	@RequestMapping("/paging")
    public String paging(HttpServletRequest request, HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	Map<String, Object> message = new HashMap<String, Object>();
    	long start = System.currentTimeMillis();
    	try{
    		if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(filePathService.getUploadFileByLimit(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
        long endTime = System.currentTimeMillis();
        System.out.println("分页获取上传的文件总计耗时："+ (endTime - startTime) +"毫秒");
        message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
    }
	
	/**
	 * 上传用户头像链接
	 * @return
	 */
	@RequestMapping("/uploadUserHeadImageLink")
	public String uploadUserHeadImageLink(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> message = new HashMap<String, Object>();
		long start = System.currentTimeMillis();
		try {
			if(!checkParams(message, request)){
				printWriter(message, response, start);
				return null;
			}
			message.putAll(userService.uploadUserHeadImageLink(getJsonFromMessage(message), getUserFromMessage(message), request));
			printWriter(message, response, start);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.put("message", EnumUtil.getResponseValue(EnumUtil.ResponseCode.服务器处理异常.value));
		message.put("responseCode", EnumUtil.ResponseCode.服务器处理异常.value);
		printWriter(message, response, start);
		return null;
	}

	
}
