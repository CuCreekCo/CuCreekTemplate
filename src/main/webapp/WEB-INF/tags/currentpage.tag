<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%--
   Parses the current page name (jsp name) from the page context
   and saves it in the request scoped currentPage variable
--%>
<c:set var="req" value="${pageContext.request}"/>
<c:set var="splits" value="${fn:split(req.requestURL,'/')}"/>
<c:forEach var="split" items="${splits}">
   <c:if test="${fn:endsWith(split,'.jsp')}">
      <c:set var="currentPage" value="${fn:substringBefore(split,'.jsp')}" scope="request"/>
   </c:if>
</c:forEach>
