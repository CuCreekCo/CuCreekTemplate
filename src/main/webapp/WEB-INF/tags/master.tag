<%@tag description="cucreek DFS Default Page Layout Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7"><![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"><![endif]-->
<!--[if gt IE 8]><!--><html class="no-js"><!--<![endif]-->
<cucreek:currentpage/>
<head>
   <link href="/resources/images/favicon.ico" rel="SHORTCUT ICON">

   <s:url value="/assets/css/jquery-ui-1.10.3.custom.min.css" var="jqueryUiCssUrl"/>
   <s:url value="/assets/css/bootstrap.min.css" var="bootstrapCssUrl"/>
   <s:url value="/assets/css/datepicker.css" var="bootstrapDatepickerCssUrl"/>
   <s:url value="/assets/css/bootstrap-notify.css" var="bootstrapNotifyCssUrl"/>
   <s:url value="/assets/css/fuelux.min.css" var="fueluxCssUrl"/>

   <s:url value="/assets/css/cucreek.css" var="cucreekCssUrl"/>

   <s:url value="/assets/js/jquery-2.0.3.min.js" var="jqueryJsUrl"/>
   <s:url value="/assets/js/jquery-ui-custom-autocomplete.min.js" var="jqueryUiJsUrl"/>
   <s:url value="/assets/js/bootstrap.min.js" var="bootstrapJsUrl"/>
   <s:url value="/assets/js/bootstrap-datepicker.js" var="bootstrapDatepickerJsUrl"/>
   <s:url value="/assets/js/bootstrap-notify.js" var="bootstrapNotifyJsUrl"/>

   <s:url value="/assets/js/fuelux.loader.min.js" var="fuelUxJsUrl"/>

   <s:message code="application_name" var="app_name" scope="request"/>
   <s:message code="application_version_name" var="app_version" scope="request"/>

   <title><s:message arguments="${app_name}" code="global_generic"/> ${title} (${currentPage})</title>


   <meta name="viewport" content="width=device-width, maximum-scale=1, initial-scale=1, user-scalable=0">

   <meta charset="utf-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
   <meta name="description" content="">
   <meta name="viewport" content="width=device-width">

   <link href="${bootstrapCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>
   <link href="${jqueryUiCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>
   <link href="${bootstrapDatepickerCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>
   <link href="${bootstrapNotifyCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>
   <link href="${fueluxCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>
   <link href="${cucreekCssUrl}" media="all" rel="stylesheet"
         type="text/css"/>

   <script src="${jqueryJsUrl}" type="text/javascript"></script>
   <script src="${jqueryUiJsUrl}" type="text/javascript"></script>
   <script src="${bootstrapJsUrl}" type="text/javascript"></script>
   <script src="${fuelUxJsUrl}" type="text/javascript"></script>


   <script src="${bootstrapDatepickerJsUrl}" type="text/javascript"></script>
   <script src="${bootstrapNotifyJsUrl}" type="text/javascript"></script>
</head>

<s:url value="/hello" var="homeUrl"/>
<s:url value="/assets/images/avatar.png" var="avatarImgUrl"/>
<s:url value="/userProfile" var="userProfileUrl"/>

<body>

<!-- TOP Main navigation bar
   ================================================== -->
<div class="navbar navbar-default navbar-fixed-top" id="main-navbar">
   <div class="container">
      <div class="navbar-header">
         <a class="navbar-brand" href="#">
            <img src="<s:url value="/assets/images/logo-image.png"/>" height="26px" alt="logo"/>
         </a>
      </div>
      <div class="collapse navbar-collapse">
         <ul class="nav navbar-nav navbar-right">
            <sec:authorize access="isAuthenticated()">
               <li>
                  <a href="#" id="pageHelpLink" class="messages"><i class="icon-question-sign"></i>&nbsp;
                     <s:message code="nav_help"/></a>
               </li>
               <li>
                  <a href="<c:url value="/resources/j_spring_security_logout"/>">
                     <s:message code="global_signout"/>
                     <i class="icon-signout"></i>
                  </a>
               </li>

               <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                     <img alt="Avatar" src="${avatarImgUrl}" height="22px"/>
                     <span>&nbsp;&nbsp;<sec:authentication property="principal.username"/></span>
                  </a>
                  <ul class="dropdown-menu">
                     <li>
                        <a href="${userProfileUrl}"><s:message
                              code="nav_user_profile"/></a>
                     </li>
                  </ul>
               </li>
            </sec:authorize>
         </ul>
         <sec:authorize access="!isAuthenticated()">
            <ul class="nav navbar-nav navbar-right">
               <li><a href="${homeUrl}">
                  <s:message code="global_signin"/>
                  <i class="icon-signin"></i>
               </a></li>
            </ul>
         </sec:authorize>

      </div>

   </div>
</div>
<!-- / Main navigation bar -->

<!-- Page Content -->
<div class="container">
   <jsp:doBody/>
</div>
<!-- / Page Content -->

<!-- Page footer -->
<hr/>

<footer id="main-footer">
   <div class="row">
      <div class="col-md-10 col-md-offset-2">
         <small>&copy; <s:message code="global_sponsored"/> ${app_name} ${app_version}</small>
      </div>
   </div>
</footer>

<!-- / Page footer -->

</body>
</html>
