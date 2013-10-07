<%@tag description="Control Group Label Normalizer" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>

<%-- This tag creates a normalized ID that is javascript safe and sets a request
context label variable.  It is used in the input-group, checkbox-group, select-group,
radio-group tags for labeling.  The labels, field help, and help text are pulled
 from the messsages.properties file.  --%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="labelPrefix" required="true"%>
<%@ attribute name="excludeField" %>
<%@ attribute name="optional"%>

<c:if test="${not empty optional}">
   <s:message code="label_optional" var="optional_label"/>
</c:if>

<c:choose>
   <c:when test="${fn:indexOf(id, '[')<0}">
      <%-- If the exclude field is present, exclude it --%>
      <c:choose>
         <c:when test="${not empty excludeField}">
            <c:set var="stripExcludeFieldUnormalized"
                   value="${fn:substringBefore(id,excludeField)}${fn:substringAfter(id,excludeField)}"/>
            <c:set var="stripExcludeField"
                   value="${fn:replace(stripExcludeFieldUnormalized, '.','')}"></c:set>
         </c:when>
         <c:otherwise>
            <c:set var="stripExcludeField" value="${id}"/>
         </c:otherwise>
      </c:choose>

      <c:set var="normalizedId"
             value="${fn:replace(
                        fn:replace(
                           fn:replace(id, '.', ''),
                              '[',''),
                                 ']','')}" scope="request"/>

      <%-- Useful for jQuery queries on paths that have a period in them - need to escape with \\. --%>
      <c:set var="escapedId"
             value="${ fn:replace(id, '.', '\\\.')}" scope="request"/>

      <s:message code="${labelPrefix}_${stripExcludeField}" var="_label"/>
      <s:message code="${labelPrefix}_${stripExcludeField}_helpText" var="_helpText" text=""/>
      <s:message code="${labelPrefix}_${stripExcludeField}_helpTitle" var="_helpTitle" text=""/>
      <s:message code="${labelPrefix}_${stripExcludeField}_helpBlockText" var="_helpBlockText" text=""/>
   </c:when>
   <c:otherwise>
      <c:choose>
         <c:when test="${not empty excludeField}">
            <c:set var="stripExcludeArr"
                   value="${fn:substringBefore(id,'[')}${fn:substringAfter(id,'].')}"/>
            <c:set var="stripArr"
                   value="${fn:replace(stripExcludeArr, excludeField,'')}"/>
         </c:when>
         <c:otherwise>
            <c:set var="stripArr"
                   value="_${fn:substringBefore(id,'[')}${fn:substringAfter(id,'].')}"/>
         </c:otherwise>
      </c:choose>

      <c:set var="normalizedId"
             value="${fn:replace(
                        fn:replace(
                           fn:replace(id, '.', ''),
                              '[',''),
                                 ']','')}" scope="request"/>

      <%-- Don't use the normalized id as an id in repeating groups --%>

      <s:message code="${labelPrefix}_${stripArr}" var="_label" text=""/>
      <s:message code="${labelPrefix}_${stripArr}_helpText" var="_helpText" text=""/>
      <s:message code="${labelPrefix}_${stripArr}_helpTitle" var="_helpTitle" text=""/>
      <s:message code="${labelPrefix}_${stripArr}_helpBlockText" var="_helpBlockText" text=""/>
   </c:otherwise>
</c:choose>

<c:set var="label" scope="request" value="${_label}"/>
<c:set var="helpTitle" scope="request" value="${_helpTitle}"/>

<c:choose>
   <c:when test="${not empty optional_label}">
      <c:set var="helpText" scope="request" value="${optional_label} ${_helpText}"/>
      <c:set var="helpBlockText" scope="request" value="${optional_label} ${_helpBlockText}"/>
   </c:when>
   <c:otherwise>
      <c:set var="helpText" scope="request" value="${_helpText}"/>
      <c:set var="helpBlockText" scope="request" value="${_helpBlockText}"/>
   </c:otherwise>
</c:choose>