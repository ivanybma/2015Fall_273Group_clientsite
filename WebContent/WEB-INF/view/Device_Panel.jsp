<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js" type="text/javascript"></script> 
<title>Device Panel</title>

<style>
 .dy_txt_old{
	display: none;
}

td{
border: 1px solid #0A1200;
vertical-align:top;
}

#div_layout {
    background-color: lightgrey;
    width: 1122px;
    padding: 10px;
    border: 10px solid navy;
    margin: 10px;
}

#group_div {
    background-color: lightgrey;
    width: 300px;
    padding: 1px;
    border: 1px solid navy;
    margin: 5px;
    overflow: auto;
    height: 200px;
}

table, th, td {
    border: 1px solid black;
}
table{
    width: 800px;
}
/* td{
	width: 260px;
} */

button{
width:100px;
}

#bootstrap_time{
width:100px;
}

button{
	background-color: #F5F5DC;
}
.clear{
	clear: both;
}

.green{
	color: #8aaf0d;
}

.box_shadow{
	background: #f3f4f6;
	border: 1px solid #e4e4e4;
	-moz-box-shadow: 0px 0px 2px 1px #e5e5e5;
	-webkit-box-shadow: 0px 0px 2px 1px #e5e5e5;
	box-shadow: 0px 0px 2px 1px #e5e5e5;
}

#message_container{
	width: 300px;
	margin: 0 auto;
	background: #fff;
	height: 200px;
	padding: 1px 0 0 0;
}

#messages li span.name{
	color: #8aaf0d;
}

#messages li span.time{
	color: #800f9d;
}
#messages {
	margin: 0;
	padding: 0;
	height: 200px;
	overflow: scroll;
	overflow-x: hidden;
}
#messages li{
	list-style: none;
	font-family: 'Open Sans', sans-serif;
	font-size: 16px;
	padding: 1px 1px;
}
#message li span.red{
	color: #e94e59;
}

#input_message_container{
	margin: 10px 10px 0 10px
}

#input_message {
	background: #f0f0f0;
	border: none;
	font-size: 15px;
	font-family: 'Open Sans', sans-serif;
	outline: none;
	padding: 1px;
	float: left;
	margin: 5px;
	width: 270px;
}

#send_msg{
	float: left;
	margin: 0;
	border: none;
	color: #fff;
	font-family: 'Open Sans', sans-serif;
    background: #96be0e;
    outline: none;
    padding: 5px 10px;
    font-size: 20px;
    cursor: pointer;
}
</style>

<script>
$(document).ready(function(){
	var startdatetime;
	var lstgroup="";
	getcurrentdatetime();
	callAjax();
	getchanges();
	refreshgroup();
	
	
	
	
$("#bootstrap").click(function(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/bootstrap", 
    	type: "POST",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val(result);
    }});
});

$("#update").click(function(){
	
	var rsc_arr = [];
	$("#rsc_detail #dy_created").each(function() {
		
		if($(this).find("input.dy_txt").val()!=$(this).find("input.dy_txt_old").val())
		{
			
	       var quantity2 = $(this).find("input.dy_txt").attr("name")+"!"+$(this).find("input.dy_txt").val();
	       rsc_arr.push(quantity2);
	       
	       $(this).find("input.dy_txt_old").val($(this).find("input.dy_txt").val());
		}
	});	
	var myJsonString = JSON.stringify(rsc_arr);
	
    $.ajax({url: "http://localhost:8080/Restful_Client_Web/update", 
    	type: "POST",
    	DataType: "json",
    	contentType: 'application/json',
    	data: myJsonString,
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val("Update done");
    }}); 
});

$("#register").click(function(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/register", 
    	type: "POST",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val("Register done");
    }});
});

$("#deregister").click(function(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/deregister", 
    	type: "POST",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val("De-register Done.");
    }});
});

$("#sleep").click(function(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/sleep", 
    	type: "POST",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val(result);
    }});
});

$("#send_msg").click(function(){
    $.ajax({url: "http://localhost:8080/Restful_Client_Web/sendmsg/"+$("#input_message").val(), 
    	type: "GET",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
    		$("#input_message").val(""); 
    		//refreshgroup();
/*             var li = "<li><span class='name'>" + $("#input_message").val() + "</span>"
            + "</li>";
    		$("#messages").append(li);
    		$("#messages").scrollTop($("#messages").height());
        $("#input_message").val(""); */
    }}); 
});


$("#activate").click(function(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/activate", 
    	type: "POST",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
        $("#bootstrap_time").val(result);
    }});
});

function callAjax(){

    $.ajax({url: "http://localhost:8080/Restful_Client_Web/rslst", 
    	type: "GET",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
 	        /* window.location.reload(); */
 	        
 	        var currentcnt =  $("#rsc_detail #dy_created").length;
 	       	var dbcount = $.parseJSON(result).length;
 	       	
 	       	if(currentcnt != dbcount)
 	       {
 	        $("#rsc_detail #dy_created").remove();
	    	$.each($.parseJSON(result), function(idx, obj) {
	            var newtr = $('<tr id="dy_created">').append(
	                    $('<td>').append(
	                    		$('<input type="text" name='+obj.objid+' readonly>').val(obj.objiddes)	),
	                    $('<td>').append(
	    	               		$('<input type="text" name='+obj.objist+' readonly>').val(obj.objist)	),
	    	            $('<td>').append(
	    	                    $('<input type="text" name='+obj.rscid+' readonly>').val(obj.rsciddes)	),
	                    $('<td>').append(
	                    		$('<input type="text" class="dy_txt" id =val'+obj.objid+obj.objist+obj.rscid+' name='+obj.objid+'.'+obj.objist+'.'+obj.rscid+'>').val(obj.rscval)	)
	                    		.append($('<input type="text" class="dy_txt_old">').val(obj.rscval))
	                );
	            $("#rsc_detail").append(newtr);
	           // alert('<input type="text" class="dy_txt" id ="val'+obj.objid+obj.objist+obj.rscid+'" name='+obj.objid+'.'+obj.objist+'.'+obj.rscid+'>');
	    	});
 	       }
 	       	
 	// refresh group msg session
 	
 	
 	   
	    	 
    },
    complete: function() {
        setTimeout(callAjax, 10000);
      }
        });
};
/* 
setInterval(callAjax,20000); */


function getchanges(){
	
	
    $.ajax({url: "http://localhost:8080/Restful_Client_Web/cmdchange", 
    	type: "GET",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
 	        /* window.location.reload(); */
	    	$.each($.parseJSON(result), function(idx, obj) {
	    		var rscobj = obj.objid+obj.objist+obj.rscid;
	    		if(obj.cmd=="light_on")
					$("#rsc_detail #val"+rscobj).css('background-color', 'red');
	    		if(obj.cmd=="light_off")
	    			$("#rsc_detail #val"+rscobj).css('background-color', 'white')
	    	});
	    	 
    },
    complete: function() {
        setTimeout(getchanges, 5000);
      }
        });
};

function getcurrentdatetime(){
	var newDate = new Date();
	var startdate =
		newDate.getFullYear().toString()+(newDate.getMonth()+1).toString()+(((newDate.getMonth()+1) < 10)?"0":"").toString() +(((newDate.getDate() < 10)?"0":"") + newDate.getDate()).toString();
	var starttime = 
		(((newDate.getHours() < 10)?"0":"") + newDate.getHours()).toString()+ (((newDate.getMinutes() < 10)?"0":"") + newDate.getMinutes()).toString()+ (((newDate.getSeconds() < 10)?"0":"") + newDate.getSeconds()).toString();
	startdatetime = startdate+starttime;
	
}

function refreshgroup(){
	
    $.ajax({url: "http://localhost:8080/Restful_Client_Web/rtvgpmsg", 
    	type: "GET",
    	DataType: "text",
    	error: function(xhr){
            alert("An error occured: " + xhr.status + " " + xhr.statusText);
        }, 
    	success: function(result){
    		/* alert(result); */
    		if(result!="")
    		{
    		if(lstgroup!=$.parseJSON(result).groupname)
    			{
    				$("#messages").empty();
    				lstgroup = $.parseJSON(result).groupname;
    			}

   				$("#active_group_name").text("Active Group: "+ $.parseJSON(result).groupname);
   				
   				var cnt=0;
  	    	$.each($.parseJSON(result).msglist, function(idx, obj) {
  	    		
  	    		var msgtime = obj.datetime.replace("_","");
  	    		//alert(starttime + " " + msgtime);
  	    		if(msgtime>startdatetime)
  	    		{
  	    			cnt++;
	            	var li = "<li><span class='name'>" + obj.sender.substring(obj.sender.lastIndexOf("-")+1) + 
	            	"</span></li><li><span> " + obj.content + "</span></li><li><span class='time'> Time: "+obj.datetime+"</span></li><p></p>";
	    			$("#messages").append(li);
  	    		}
	    	}); 
  	    //	alert($("#messages").height());
  	    if(cnt>0)
  	    	{
  	    	$("#messages").scrollTop($("#messages").height());
  	    	
  	  		getcurrentdatetime();
  	    	}
	        
	        
    	}
    	else{
    		$("#messages").empty();
    		$("#active_group_name").text("Active Group: ");
    		}
    },
     complete: function() {
        setTimeout(refreshgroup, 1000);
      } 
        });
};

});
</script>

</head>
<body>
<div id = "div_layout">
<table>
<tr><td>
<table id="rsc_detail">
<tr>
<td><button type="button" id="bootstrap" onclick="">BootStrap</button></td><td><input type="text" id = "bootstrap_time" readonly></td>
<td><button type="button" id="update" onclick="">Update device info</button></td>
</tr>
<tr>
<td><button type="button" id="deregister" onclick="">De-register</button></td>
<td><button type="button" id="register" onclick="">Register</button></td>
<td><button type="button" id="sleep" onclick="">Sleep</button></td>
<td><button type="button" id="activate" onclick="">Activate</button></td>
</tr>
<tr>
<td>Object</td><td>Object instance</td><td>Resource</td><td>Resource Value</td>
</tr>
</table>
</td>
<td>
<p id = "active_group_name">Active Group:</p>
<div id="message_container" class="box_shadow">
<ul id="messages">
</ul></div>
<div id="input_message_container">
<input type="text" id="input_message"
placeholder="Type your message here..." /> 
<button type="button" id="send_msg" onclick="">Send</button>
<div class="clear"></div>
<!-- <textarea id ="new_msg" rows="4" cols="40"> -->
<!-- </textarea> -->
</div>
<!-- <div><button type="button" id="send_msg" onclick="">Send</button></div> -->
</td>
</tr>
</table>



</div>
</body>
</html>