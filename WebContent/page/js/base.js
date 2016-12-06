/**
 * 判断字符串是否为空
 * @param str
 */
function isEmpty(str){
	return str == null || str == undefined || str == '' || str.trim == '';
}

/**
 * 判断字符串不为空
 * @param str
 */
function isNotEmpty(str){
	return !isEmpty(str);
}

/**
 * 校验连接是否合法
 * @param link
 */
function isLink(link){
	return true;
}

/**
 * 对空的字符串，以""输出
 * @param str
 * @returns
 */
function changeNotNullString(str){
	if(isEmpty(str))
		return "";
	return str;
}

/**
 * 将form的serializeArray数组转化成json对象
 * @param array
 */
function serializeArrayToJsonObject(array){
	var json = {};
	if(array.length > 0){
		for(var i = 0; i < array.length; i++){
			var o = array[i];
			json[o.name] = o.value;
		}
	}
	return json;
}

/**
 * 添加cookie
 * @param key
 * @param value
 */
function addCookie(key, value){
	window.cookie  = key +'=' +value;
}

/**
 * 获取cookie
 * @param value
 */
function getCookie(value){
	if (document.cookie.length > 0){//先查询cookie是否为空，为空就return ""
		c_start = document.cookie.indexOf(value + "=")//通过String对象的indexOf()来检查这个cookie是否存在，不存在就为 -1　　
		if (c_start!=-1){
			c_start = c_start + value.length + 1;//最后这个+1其实就是表示"="号啦，这样就获取到了cookie值的开始位置
			c_end = document.cookie.indexOf(";",c_start);//其实我刚看见indexOf()第二个参数的时候猛然有点晕，后来想起来表示指定的开始索引的位置...这句是为了得到值的结束位置。因为需要考虑是否是最后一项，所以通过";"号是否存在来判断
			if (c_end == -1) 
					c_end = document.cookie.length;
			return unescape(document.cookie.substring(c_start, c_end));//通过substring()得到了值。想了解unescape()得先知道escape()是做什么的，都是很重要的基础，想了解的可以搜索下，在文章结尾处也会进行讲解cookie编码细节
		}
	}
	return ""
}