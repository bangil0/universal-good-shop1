
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Shop::Login</title>
<link rel="icon" href="<c:url value="/res/img/javaEE.ico"></c:url >"
	type="image/x-icon">
<link rel="stylesheet" type="text/css"
	href=<c:url value="/res/css/bootstrap.css?version=1"></c:url> />
<link rel="stylesheet" type="text/css"
	href=<c:url value="/res/css/shop.css?version=1"></c:url> />
<script src="<c:url value="/res/js/bootstrap.js"></c:url >"></script>
<script src="<c:url value="/res/js/ajax.js"></c:url >"></script>
<script src="<c:url value="/res/js/util.js"></c:url >"></script>
<script type="text/javascript">
	function login() {

		var username = document.getElementById("useraname").value;
		var password = document.getElementById("password").value;
		var request = new XMLHttpRequest();
		infoLoading();
		var requestObject = {
			'user' : {
				'username' : username,
				'password' : password
			}
		}
		postReq(
				"<spring:url value="/api/account/login" />",
				requestObject,
				function(xhr) {
					var response = (xhr.data);
					if (response != null && response.code == "00") {
						alert("LOGIN SUCCESS");
						window.location.href = "<spring:url value="/admin/home" />";
					} else {
						alert("LOGIN FAILS");
					}
				});
	}

	function goToRegister() {
		window.location.href = "<spring:url value="/account/register" />";
	}
</script>
</head>
<body>
	<div id="loading-div"></div>
	<div class="body">
		<p id="info" align="center"></p>
		<div class="wrapper-login-form">

			<div class="login-form">

				<span style="font-size: 2em;">Silakan Login</span> <br> <label
					for="useraname"> Username </label> <br> <input id="useraname"
					class="form-control" type="text" /> <br /> <label for="password">
					Kata sandi </label> <br>
				<input id="password" type="password" class="form-control" /> <br />
				<button class="btn btn-default" onclick="login(); return false;">Login</button>
				<button class="btn btn-default"
					onclick="goToRegister(); return false;">Register</button>

			</div>
		</div>
	</div>
</body>
</html>