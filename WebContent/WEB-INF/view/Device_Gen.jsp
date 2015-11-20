<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Device creation Panel</title>

<script>
$(document).ready(function(){


$(function() {
    $('#createForm').submit(function() {
        // DO STUFF
        window.open("http://localhost:8080/Restful_Client_Web/new_device",$('#dev_name').val(), "width=500,height=500,scrollbars=no,resizable=no");
     	//alert($('#dev_name').val());
        return false; // return false to cancel form action
    });
});

$("button").click(function(){
    $.ajax({url: "demo_test.txt", success: function(result){
        $("#div1").html(result);
    }});
});

});
</script>
</head>
<body>
<p>Please generate any new devices by clicking the button</p>

<form id = "createForm" action="new_device" method = "get">
Device Name: <input  type="text" id = "dev_name" name="name">
<input type="submit" value="Activate a new device">
</form>

</body>
</html>