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
    <li>Buy Books<a href="MyServlet?uname=<%= uname %>&iden=browse">Books</a></li>
    <li>Add Money<a href="addmoney.html">MONEY</a></li>
  </ul></nav>

<section>  
  <h1>Bookstore</h1>
  <h2>Created by Srinivas</a></h2>
</section>

</body>

</html>