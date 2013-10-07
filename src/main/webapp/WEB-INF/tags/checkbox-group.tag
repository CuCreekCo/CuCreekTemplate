<%@tag description="Control Group Checkbox Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<%@ attribute name="id" required="true" %>
<%-- converted to Spring path --%>
<%@ attribute name="binding" required="true" %>
<%-- to determine binding errors --%>
<%@ attribute name="labelPrefix" required="true" %>
<%-- prefix for the label to grab from message.properties prefix_I
D --%>
<%@ attribute name="selectList" type="java.util.List" %>
<%-- optional checkbox list --%>
<%@ attribute name="value" type="com.cucreek.web.controller.persistence.dto.CodeTableDTO" %>
<%@ attribute name="excludeField" %>
<%@ attribute name="readOnly" %>
<%@ attribute name="suppressHelp" %>

<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                      excludeField="${excludeField}" optional="${optional}"/>

<s:bind path="${binding}.${id}">
   <c:set var="bindErrors" value="${status.errorCode}"/>
</s:bind>

<div class="form-group ${not empty bindErrors?'has-error':''}">
   <label class="control-label" for="${normalizedId}">${label}</label>
   <c:choose>
      <c:when test="${not empty selectList}">
         <c:choose>
            <c:when test="${not empty readOnly}">
               <div class="checkbox">
                  <sf:checkboxes items="${selectList}" path="${id}" itemLabel="description"
                                 disabled="true" id="${normalizedId}"/>
               </div>
            </c:when>
            <c:otherwise>
               <div class="checkbox">
                  <sf:checkboxes items="${selectList}" path="${id}" itemLabel="description" id="${normalizedId}"/>
               </div>
            </c:otherwise>
         </c:choose>
      </c:when>
      <c:otherwise>
         <c:choose>
            <c:when test="${not empty readOnly}">
               <div class="checkbox">
                  <sf:checkbox path="${id}" value="${value.codeValue}" disabled="true"
                               label="${value.description}" id="${normalizedId}"/>
               </div>

            </c:when>
            <c:otherwise>
               <div class="checkbox">
                  <sf:checkbox path="${id}" value="${value.codeValue}" id="${normalizedId}"
                               label="${value.description}"/>
               </div>

            </c:otherwise>
         </c:choose>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty bindErrors}">
			<span class="help-block">
            <span class="glyphicon glyphicon-exclamation-sign"></span>
				<sf:errors path="${id}"/>		
			</span>
   </c:if>
   <c:if test="${not empty helpText && empty readOnly && empty suppressHelp}">
			<span class="help-block">
				<cucreek:popover id="${normalizedId}Popover" text="${helpText}"
                             title="${helpTitle}"/>
				${helpBlockText}
			</span>
   </c:if>
</div>
