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

#div_layout {
    background-color: lightgrey;
    width: 800px;
    padding: 10px;
    border: 10px solid navy;
    margin: 10px;
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
</style>

<script>
$(document).ready(function(){
	callAjax();
	getchanges();
	
	
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

});
</script>

</head>
<body>
<div id = "div_layout">
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
<div id="msg_div"></div>
</div>
</body>
</html>