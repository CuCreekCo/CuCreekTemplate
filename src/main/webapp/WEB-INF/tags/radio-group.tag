<%@tag description="Control Group Radio Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<%--
   id - gets converted to the spring binding path
   binding - is the spring binding object
   labelPrefix - used to lookup field in the messages.properties file
   selectList - optional select list that will render a group of radio buttons
   value - a DFS CodeTableDTO carries the radio object selected
   excludeField - when searching for the message in  messages.properties exclude this
      string in the lookup value.  I.e. passField_headerFields.passStyle can exclude
      the "headerFields" so that the same message could be used for primaryFields or:
         passField.passStyle
--%>

<%@ attribute name="id" required="true" %>
<!-- converted to Spring path -->
<%@ attribute name="binding" required="true" %>
<!-- to determine binding errors -->
<%@ attribute name="labelPrefix" required="true" %>
<!-- prefix for the label to grab from message.properties prefix_ID -->
<%@ attribute name="selectList" type="java.util.List" %>
<!-- optional radio list -->
<%@ attribute name="selectArray" type="java.lang.Object[]" %>
<!-- optional radio array -->
<%@ attribute name="value" type="gov.mt.cucreek.dfs.persistence.dto.CodeTableDTO" %>
<%@ attribute name="excludeField" %>
<%@ attribute name="optional" %>
<%@ attribute name="suppressHelp" %>

<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                      excludeField="${excludeField}" optional="${optional}"/>

<s:bind path="${binding}.${id}"> <!-- sets a status variable with BindStatus fields -->
   <c:set var="bindErrors" value="${status.errorCode}"/>
</s:bind>
<div class="form-group ${not empty bindErrors?'has-error':''}">
   <label class="control-label" for="${id}">${label}</label>
   <c:choose>
      <c:when test="${not empty selectList}">
         <div class="radio">
            <sf:radiobuttons items="${selectList}" path="${id}" itemLabel="descr" id="${normalizedId}"/>
         </div>
      </c:when>
      <c:when test="${not empty selectArray}">
         <div class="radio">
            <sf:radiobuttons items="${selectArray}" path="${id}" id="${normalizedId}"/>
         </div>
      </c:when>
      <c:otherwise>
         <div class="radio">
            <sf:radiobutton path="${id}" value="${value}" id="${normalizedId}"/>
            label="${value.descr}"/>
         </div>
      </c:otherwise>
   </c:choose>
   <c:if test="${not empty bindErrors}">
			<span class="help-block">
            <span class="glyphicon glyphicon-exclamation-sign"></span>
				<sf:errors path="${id}"/>		
			</span>
   </c:if>
   <c:if test="${empty suppressHelp and not empty helpText}">
			<span class="help-block">
				<cucreek:popover id="${normalizedId}Popover" text="${helpText}"
                             title="${helpTitle}"/>
				${helpBlockText}
			</span>
   </c:if>
</div>
