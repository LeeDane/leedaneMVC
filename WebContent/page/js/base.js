/**
 * 判断字符串是否为空
 * @param str
 */
function isEmpty(str){
	return typeof(str) == 'undefined' || str == null || str == undefined || str == '' || str.trim == '';
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
 * 对空的字符串，以""输出
 * @param str
 * @param key
 * @returns
 */
function changeObjNotNullString(obj, key){
	if(typeof(obj) == 'undefined')
		return '';
	if(isEmpty(obj[key]))
		return "";
	return obj[key];
}


/**
 * 展示图片的链接
 * @param index  当前心情的索引
 * @param imgIndex 当前心情图片的索引
 */
function showSingleImg(obj){
	var path = $(obj).attr("src");
	if(isNotEmpty(path)){
		var json = {
				  "title": "相册标题", //相册标题
				  "id": 0, //相册id
				  "start": 0 //初始显示的图片序号，默认0
				};
		var datas = new Array();
		var each = {};
		each.src = path;//原图地址
		each.alt = path;//缩略图地址
		datas.push(each);
		
		
		json.data = datas;
		
		layer.photos({
		    photos: json
		    ,shift: 1 //0-6的选择，指定弹出图片动画类型，默认随机
		  });
	}else{
		layer.msg("无法获取当前图片的路径");
	}
	
}

/**
 * 展示图片的链接
 * @param index  当前心情的索引
 * @param imgIndex 当前心情图片的索引
 */
function showImg(index, imgIndex){
	var mood = moods[index];
	var json = {
			  "title": "相册标题", //相册标题
			  "id": 0, //相册id
			  "start": imgIndex //初始显示的图片序号，默认0
			};
	var datas = new Array();
	var photos = mood.imgs.split(";");
	for(var i = 0; i < photos.length; i++){
		var each = {};
		var path = photos[i];
		each.src = path;//原图地址
		each.alt = path;//缩略图地址
		datas.push(each);
	}
	
	json.data = datas;
	
	layer.photos({
	    photos: json
	    ,shift: 1 //0-6的选择，指定弹出图片动画类型，默认随机
	  });
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
 * 跳转到我的个人中心
 * @param id
 */
function linkToMy(id){
	if(isEmpty(id)){
		layer.msg("该用户不存在，请联系管理员核实");
		return;
	}
	window.open(getBasePath() +"page/my.jsp?uid="+id, "_self");
}

/**
 * 跳转到搜索
 */
function linkToSearch(searchKey){
	window.open(getBasePath() + 'page/search.jsp?q='+searchKey+'&t='+Math.random(), '_blank');
}

/**
 * 跳转到全文阅读
 * @param id
 */
function goToReadFull(id){
	//layer.msg("文章ID为："+id);
	if(isEmpty(id)){
		layer.msg("该博客不存在，请联系管理员核实");
		return;
	}
	window.open(getBasePath() +"page/detail.jsp?bid="+id, "_blank");
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


/**
 * 对数组对象进行排序，格式[{name: 'name', total: 0}, {name: 'name1', total: 100}]
 * @param key  根据上面格式，total可以传进去排序
 * @param desc  是否倒序排序(从大到小)，默认是
 * @returns {Function}
 */
function sortByObjectKey(key, desc) {
	return function(a,b){
		var value1 = a[key];
		var value2 = b[key];
		if(value1 < value2){
	        return desc ? 1 : -1;
	    }else if(value1 > value2){
	        return desc ? -1 : 1;
	    }else{
	        return 0;
	    }
	}
}

/**
 * 将日期字符串转成日期时间格式
 * @param time
 */
function formatStringToDateFormattime(str){
	return str.replace("T", " ") + ":00";
}
/**
 * 格式化日期格式
 * @param time
 */
function formatDateTime(time){
	var now;
	if(isNotEmpty(time))
		now = new Date(time);
	else
		now = new Date();
	
	return now.getFullYear() + "-" + fix((now.getMonth() + 1),2) + "-" + fix(now.getDate(),2) + "T" + fix(now.getHours(),2) + ":" + fix(now.getMinutes(),2);
}
function fix(num, length) {
	 return ('' + num).length < length ? ((new Array(length + 1)).join('0') + num).slice(-length) : '' + num;
}


/**
 * 获取url地址中的参数的方法
 * @param url url地址
 * @param sProp  参数的名称
 * @returns
 */
function getURLParam(url, sProp) {
	if(!url)
		url = window.location.href; //取得当前的饿地址栏地址信息
	
	// 正则字符串
	var re = new RegExp("[&,?]" + sProp + "=([^\\&]*)", "i");
	// 执行正则匹配
	var a = re.exec(url);
	if (a == null) {
		return "";
	}
	return a[1];
}

/**
 * 获取随机整数(不包括0，包括number)
 * @param number
 */
function getRandomNumber(number){
	return Math.ceil(Math.random() * number);
}

/**
 * 获取随机整数(包括0，不包括number)
 * @param number
 */
function getRandomNumber1(number){
	return Math.floor(Math.random() * number);
}

/**
 * 检查用户浏览器是否是IE内核
 * @returns {Boolean}
 */
function checkIE(){
	userAgent=window.navigator.userAgent.toLowerCase();
	if(userAgent.indexOf("firefox") >= 1){
		
	}else {
	    var name=navigator.appName;
	    if(name=="Microsoft Internet Explorer"){
	    	return true;
	    }
	}
	return false;
}
