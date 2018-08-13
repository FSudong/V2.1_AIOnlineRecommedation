<%@ page import="java.net.URLEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: yaosheng
  Date: 2017/5/22
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    //获得当前url
    String url_str = new String(request.getRequestURL());

    String[] temp = url_str.split(".jsp");
    url_str = temp[0];


    //判断当前url是否有参数
    if(request.getQueryString()!=null && !"".equals(request.getQueryString())){
        url_str = url_str.concat("?" + request.getQueryString());
    }
    String url = URLEncoder.encode(url_str);

%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>AI</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="alternate icon" type="image/png" href="./resources/i/favicon.png">
    <link rel="stylesheet" href="<%=basePath%>resources/css/style.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=basePath%>resources/css/bootstrap.min.css">

    <script type="text/javascript" src="<%=basePath%>resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/js/jquery.json-2.2.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/js/paneltabchange.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/js/search.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/js/save.js"></script>
</head>
<body>
<header class="am-topbar">
    <nav role="navigation" class="navbar navbar-default">

        <div class="navbar-header">
            <button data-target="#bs-example-navbar-collapse-1" data-toggle="collapse" class="navbar-toggle" type="button">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="./" class="navbar-brand">AIOnline</a>
        </div>
        <div id="bs-example-navbar-collapse-1" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <c:if test="${tag == 0}">
                    <li><a href="./todayArxiv">今日Arxiv</a></li>
                    <%--<li><a href="./recommender">Arxiv 推荐</a></li>--%>
                </c:if>
                <c:if test="${tag == 1}">
                    <li class="active"><a href="./todayArxiv">今日Arxiv</a></li>
                    <%--<li><a href="./recommender">Arxiv 推荐</a></li>--%>
                </c:if>

                <c:if test="${tag == 2}">
                    <li><a href="./todayArxiv">今日Arxiv</a></li>
                    <%--<li class="active"><a href="./recommender">Arxiv 推荐</a></li>--%>
                </c:if>

                <%--<li id="dropdown-tab2" class="dropdown">--%>
                    <%--<a data-toggle="dropdown" class="dropdown-toggle" href="#">DBLP<b id="dropdown-square2" class="caret"></b></a>--%>
                    <%--<ul id="dropdown-panel2" role="menu" class="dropdown-menu">--%>
                        <%--<li><a href="#">Conferences</a></li>--%>
                        <%--<li><a href="#">Journals</a></li>--%>
                    <%--</ul>--%>
                <%--</li>--%>
            </ul>
            <form id=“search” role="search" class="navbar-form navbar-left" style="width:55%;margin-left:10%" action="./search" onsubmit="return submitForm(this)" method="post">
                <div class="form-group" style="width: 90%">
                    <input id = "terms" name = "terms" type="text" placeholder="Search" class="form-control" style="width: 100%">
                    <input id = "tag" name = "tag" type="hidden" value="${tag}">
                </div>
                <button class="btn btn-primary" type="submit">
                    查询
                </button>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${! empty LOGIN_USER}">
                        <li id="dropdown-tab1" class="dropdown" >
                            <a class="dropdown-toggle" href="#">
                                <i class="icon-user"></i>
                                    ${LOGIN_USER.uname}
                                        <b id="dropdown-square1" class="caret"></b>
                            </a>
                            <ul id="dropdown-panel1" role="menu" class="dropdown-menu">
                                <li><a href="./userinfo">个人中心</a></li>
                                <li class="divider"></li>
                                <li><a href="login/logout">退出登录</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="login/login.jsp?next=<%=url%>">登录</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>
</header>

<div class="main-content">
    <div class="content-panel">

        <hr style="margin:0 0 20px 0;">
        <c:forEach items="${papers}" var="p" varStatus="loop">
            <article class="blog-main">
                <h4 class="am-article-title blog-title">
                    <a href="paperinfo?id=${p.id}">${p.title}</a>
                </h4>
                <c:forEach items="${authorMap.get(p.id)}" var="author" varStatus="a_loop">
                    <c:choose>
                        <c:when test="${! empty author.aid}">
                            <span><a href="${author.url}"><u style="text-decoration: none" >${author.authorname.split(",")[0]}</u></a></span>
                        </c:when>
                        <c:otherwise>
                            <span><a  href="#"><u style="text-decoration: none" >${author.authorname.split(",")[0]}</u></a></span>
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
                <span class="paper-time">
                        ${p.time.toLocaleString().split("-")[0]} ${p.time.toLocaleString().split("-")[1]}
                </span>

                <span style="float: right;font-size: 8px">
                    <c:choose>
                        <c:when test="${!startedMap.get(p.id)}">
                            <input name = "save${p.id}" id="save${p.id}" type="button" onclick="return savePaper('${p.id}',5,'${LOGIN_USER.id}',${startedMap.get(pid)})" value="收藏"/>
                        </c:when>
                        <c:otherwise>
                            <input name = "save${p.id}" id="save${p.id}" type="button" onclick="return savePaper('${p.id}',5,'${LOGIN_USER.id}',${startedMap.get(pid)})" value="已收藏"/>
                        </c:otherwise>
                    </c:choose>

                </span>

                <div class="am-g blog-content" style="text-align: justify;margin-top: 3px" >
                    <div class="am-u-lg-12">
                        <p>${p.paperAbstract}<a href="paperinfo?id=${p.id}">[more]</a></p>

                    </div>
                </div>

            </article>
            <hr>
        </c:forEach>
        <div class="paging-panel" >
            <c:choose>
                <c:when test="${paper_num>=limit}">
                    <ul>
                        <li class="btn btn-default li-left"><a href="?pageNum=${previousPage}&terms=${terms}">&laquo; 上一页</a></li>
                        <li class="btn btn-default li-right"><a href="?pageNum=${nextPage}&terms=${terms}">下一页 &raquo;</a></li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <ul>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>
<footer class="footer">

</footer>

<!--[if lt IE 9]>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="<%=basePath%>resources/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->

<!--[if (gte IE 9)|!(IE)]><!-->
<script src="<%=basePath%>resources/js/jquery.min.js"></script>

</body>
</html>
