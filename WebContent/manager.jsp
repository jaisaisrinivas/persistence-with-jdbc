<!DOCTYPE html>
<html>

<head>
<%HttpSession sess= request.getSession();
String uname=(String)sess.getAttribute("uname");
System.out.println(uname+"-----");
%>
  <meta charset="UTF-8">

  <title>Welcome <%= uname %> </title>


    <link rel="stylesheet" href="css/style.css">

</head>

<body>

<nav>
  <ul>
    <li>Home<a href="#">Home</a></li>
    <li>Add Users<a href="adduser.html">ADD</a></li>
    <li>Add Books<a href="addbooks.html">ADD</a></li>
    <li>Approve<a href="check?uname=<%= uname %>&iden=approve">ADD</a></li>
    
  </ul></nav>

<section>  
  <h1>Bookstore</h1>
  <h2>Created by Srinivas</a></h2>
</section>

</body>

</html>