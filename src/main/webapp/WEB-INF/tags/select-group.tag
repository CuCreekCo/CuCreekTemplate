<%@tag description="Control Group Select Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>

<%--
   id is the the path or unique id of the bound object
   binding is the Spring bound object
   labelPrefix is used to lookup labels, help, and help blocks
   readOnly indicates the input is ready only
   suppressHelp will not output any help
   cssClass to apply to the select widget
   showCodeValuePlusDescription to show code value - description
--%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="binding" required="true"%>
<%@ attribute name="labelPrefix" required="true"%>
<%@ attribute name="selectList" required="true" type="java.util.List"%>
<%@ attribute name="optional"%>
<%@ attribute name="suppressHelp"%>
<%@ attribute name="excludeField"%>
<%@ attribute name="readOnly"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="showCodeValuePlusDescription"%>

<s:message code="field_select" var="pleaseSelect"/>

<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                  excludeField="${excludeField}" optional="${optional}"/>

<s:bind path="${binding}.${id}">
	<c:set var="bindErrors" value="${status.errorCode}"/>
</s:bind>

<c:choose>
   <c:when test="${empty optional}">
      <s:message code="field_select" var="pleaseSelect"/>
   </c:when>
   <c:otherwise>
      <s:message code="field_select_optional" var="pleaseSelect"/>
   </c:otherwise>
</c:choose>

<div class="form-group-group ${not empty bindErrors?'has-error':''}">
	<label class="control-label" id="${normalizedId}ControlLabel" for="${normalizedId}">${label}</label>
   <c:choose>
      <c:when test="${not empty readOnly}">
         <sf:select path="${id}" disabled="true"
                    cssClass="form-control ${cssClass}" id="${normalizedId}">
            <sf:option value="" label="${pleaseSelect}"/>
            <c:choose>
               <c:when test="${empty showCodeValuePlusDescription}">
                  <sf:options items="${selectList}" itemValue="codeValue" itemLabel="description"/>
               </c:when>
               <c:otherwise>
                  <sf:options items="${selectList}" itemValue="codeValue" itemLabel="codeValuePlusDescription"/>
               </c:otherwise>
            </c:choose>
         </sf:select>
      </c:when>
      <c:otherwise>
         <sf:select path="${id}" cssClass="form-control ${cssClass}" id="${normalizedId}">
            <sf:option value="" label="${pleaseSelect}"/>
            <c:choose>
               <c:when test="${empty showCodeValuePlusDescription}">
                  <sf:options items="${selectList}" itemValue="codeValue" itemLabel="description"/>
               </c:when>
               <c:otherwise>
                  <sf:options items="${selectList}" itemValue="codeValue" itemLabel="codeValuePlusDescription"/>
               </c:otherwise>
            </c:choose>
         </sf:select>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty bindErrors}">
      <span class="help-block">
         <span class="glyphicon glyphicon-exclamation-sign"></span>
         <sf:errors path="${id}"/>
      </span>
   </c:if>
   <c:if test="${empty suppressHelp && not empty helpText && empty readOnly}">
      <span class="help-block">
         <cucreek:popover id="${normalizedId}Popover" text="${helpText}"
         title="${helpTitle}"/>
         ${helpBlockText}
      </span>
   </c:if>
</div>
