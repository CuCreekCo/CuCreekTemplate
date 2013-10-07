<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<cucreek:master>
<div class="row">
   <div class="col-md-10 col-lg-offset-2">
      <h3><s:message arguments="${app_name}" code="welcome_h3"/>&nbsp;<small> <s:message code="welcome_subtext"/></small></h3>
   </div>
</div>
<div class="row">
   <div class="col-md-4 col-md-offset-4">
      <img src="<s:url value="/assets/images/logo-image.png"/>"/>
   </div>
</div>
   <div class="row">
      <div class="col-md-offset-4 col-md-4" style="text-align: center;">
         ${date}
      </div>
   </div>
</cucreek:master>
