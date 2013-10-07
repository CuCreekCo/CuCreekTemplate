<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<cucreek:master>
   <cucreek:messages/>
   <cucreek:focus-errors focusFieldId="usernameInput"/>

   <s:url var="authUrl" value="/resources/j_spring_security_check"/>
   <c:if test="${not empty param.login_error }">
      <div class="alert alert-danger">
         <s:message code="security_login_unsuccessful"/>
      </div>
   </c:if>

   <div class="row">

   <form class="form-horizontal" method="post" action="${ authUrl }">
      <div class="form-group">
         <label for="usernameInput" class="col-md-2 control-label">
            <s:message code="security_login_form_name"/>
         </label>

         <div class="col-md-4">
            <input class="form-control" name="j_username" id="usernameInput" type="text"/>
         </div>
      </div>
      <div class="form-group">
         <label for="passwordInput" class="col-md-2 control-label">
            <s:message code="security_login_form_password"/>
         </label>

         <div class="col-md-4">
            <input class="form-control" name="j_password" type="password" id="passwordInput"/>
         </div>
      </div>
      <div class="form-group">
         <div class="col-md-offset-2 col-md-4">
            <div class="checkbox">
               <label>
                  <input name="j_spring_security_remember_me" id="remember_me" type="checkbox">
                  <s:message code="security_login_form_remember_me"/>
               </label>
            </div>
         </div>
      </div>
      <div class="form-group">
         <div class="col-md-offset-2 col-md-4">
            <input type="submit" class="btn btn-large btn-primary" value="<s:message code="global_signin"/>"/>
         </div>
      </div>
   </form>
</cucreek:master>
