<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>EZ Tracker Login</title>
		<script type="text/javascript" src="https://ajax.microsoft.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
	<!--	<script src="validation_check.js"></script>	-->
		<link rel="stylesheet" type="text/css" href="EZ.css">
		<style>

		</style>
	</head>	
<body class="loginBody" id="runningMan" background="background2.jpg">	
<?php
	
	$email = $_POST['email'];
	$password = $_POST['passwd'];
	
	$output = shell_exec("sudo python3 eztracker.py $email $password &");	
	
	$error = shell_exec("cat error.txt");
	
	if($error == 1){
		header("Location: fail.php");
		exit();
	}
	elseif($error == 2){
		header("Location: error.html");
		exit();
	}
	elseif($error == 0)
	{
		header("Location: success.html");
		exit();
	}
	else
	{
		header("Location: error.html");
		exit();
	}
	
?>	
 
 		<center><h1 style="color: white;"></h1></center>
	
		<div  id="loginWrapper">
			<form name="login" action="login.php" method="POST">
					<center><img src="ezlogotransparent.png" alt="EZ Logo" width="260px" height="190" /></center>		
				<div id="failinside">
					<br/><label for="uname" id="failPlace"><strong>Success! You may now use your device.</strong></label><br/><br/>

				</div>
						
			</form>		
		</div>	




</body>
</html>
