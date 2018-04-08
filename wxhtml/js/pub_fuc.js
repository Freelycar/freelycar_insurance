	/**
 * 
 */
$.ajaxSetup({ cache: false });

//setCookie
var setCookie = function (name,value)
{
    var exp = new Date();
    exp.setTime(exp.getTime() + 60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//getCookie
var getCookie = function (name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}
//deleteCookie 注意：参数name为key ，不是value
var deleteCookie = function ( name ) {
	  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


/**
 * 
 * 格式化日期
 * 
 */
Date.prototype.yyyymmdd = function() {   
    var yyyy = this.getFullYear().toString();                                    
    var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based         
    var dd  = this.getDate().toString();             
    
    var HH = this.getHours().toString();       //获取当前小时数(0-23)
    var MM = this.getMinutes().toString();     //获取当前分钟数(0-59)
    var ss = this.getSeconds().toString();      
    return yyyy + '-' + (mm[1]?mm:"0"+mm[0]) + '-' + (dd[1]?dd:"0"+dd[0])+ ' ' +(HH[1]?HH:"0"+HH[0])+ ':' +(MM[1]?MM:"0"+MM[0])+ ':' +(ss[1]?ss:"0"+ss[0]);
};  



/**
 * 
 */

var curpage =1;
var  temp = "";
var pagebutton = function(pageNums,number){
	$(".pageButton").click(function(){
		//再去查一共多少页
		var name = $(this).attr("name");
			if(name=="sy"){
				curpage = 1;
			}else if(name=="syy"){
				curpage = --curpage>1?curpage:1;
			}else if(name=="xyy"){
				curpage = ++curpage>pageNums?pageNums:curpage;
			}else if(name=="wy"){
				curpage=pageNums;
			}
			
			if(curpage!=temp){
	 			loadData(curpage);
	 		}
			temp = curpage;
			/*$("#showCurrnetPage").text(curpage+"/"+pageNums);*/
			
		});
	
}

/**
 * 删除表格中的一个
 * 
 * 
 */

var deleteItem = function(source,url){
	$(source).click(function(){
		var t_id = $(this).attr("t_id");
		$.getJSON(url,{"id":id},function(data){
			var code = data.code;
			if(code=="0"){
				window.location.reload();
			}
		});
	});
}



/**
 * 获取全部院系
 */
var getAlldepartment = function(){
	$.getJSON("api/department/all",function(data){
		var code = data.code;
		var arr = data.data;
		if(code=="0"){
			$.each(arr,function(){
				var html ='<option value="'+this.name+'">'+this.name+'</option>';
				$("#all_department").append(html);
				$(".all_department").append(html);
			});
			
		}
	});
}

/**
 * 获取全部年级
 */
var getAllyear = function(){
	$.getJSON("api/year/all",function(data){
		var code = data.code;
		var arr = data.data;
		if(code=="0"){
			$.each(arr,function(){
				var html ='<option value="'+this.name+'">'+this.name+'</option>';
				$("#all_year").append(html);
				$(".all_year").append(html);
			});
			
		}
	});
}

/**
 * 获取全部科目
 * 
 */
var getAllSubject = function(){
	$.getJSON("api/subject/all",function(data){
		var code = data.code;
		var arr = data.data;
		if(code=="0"){
			$.each(arr,function(){
				var html ='<option value="'+this.id+'">'+this.name+'</option>';
				$("#all_subject").append(html);
			});
			
		}
	});
}


/**
 * 
 * 获取全部班级
 * 
 */

var getAllClasses = function(dept,year){
	
	$.getJSON("api/classes/findAll",{deptName:dept,year:year},function(data){
		var code = data.code;
		var arr = data.data;
		if(code=="0"){
			$.each(arr,function(){
				var html ='<option value="'+this.name+'">'+this.name+'</option>';
				$("#all_classes").append(html);
			});
			
		}
	});
	
}




/**
 * 解析url中的参数
 * 
*/

var getParameterByName = function (name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


/**
 * 计算列表中的序号 base 1
 * @param start:第几页
 * @param number:每页的数量
 * @param index:在该页的index base 0
 * 
 */
var getPageIndex = function(start,number,index){
	return (start-1)*number+index+1;
}

var justifyNull =function (obj) {
    for (let key in obj) {
      if (!obj[key]) {
        obj[key] = '';
      }
    }
  }

  function timestampToTime(timestamp, sec) {
    if (!timestamp) {
      return ''
    }
    var date = new Date(timestamp * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = date.getDate() + ' ';
    let h = date.getHours() + ':';
    let m = date.getMinutes() + ':';
    let s = date.getSeconds();
    if (sec) {
      return Y + M + D + h + m + s;
    }
    return Y + M + D;
  }
  
  
