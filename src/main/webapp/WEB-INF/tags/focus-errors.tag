<%@tag description="Display a Field and Label" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>

<%--

   focusFieldId - the ID of the field to focus on
   topPaddingAdjustment - adjust the top padding so the scrolling works better (optional)
--%>

<%@ attribute name="focusFieldId" required="true" %>
<%@ attribute name="topPaddingAdjustment" type="java.lang.Integer" %>

<script type="text/javascript">
   $(function(){
   /*
    Set the field in error focus.
    First, get the body top padding CSS amount to set the following scrollTops
    */
   var bodyPaddingTop = $("body div.container").position().top;
   <c:choose>
      <c:when test="${not empty topPaddingAdjustment}">
         var _paddingAdjustment = ${topPaddingAdjustment}; /* account for error message and field report wizard */
      </c:when>
      <c:otherwise>
         var _paddingAdjustment = 0;
      </c:otherwise>
   </c:choose>

   var fieldInError = $("span.help-block span.glyphicon-exclamation-sign");
   if (fieldInError.length > 0)
   {
      $(window).scrollTop(fieldInError.first().position().top - bodyPaddingTop - _paddingAdjustment);
      fieldInError.first().parent().siblings("input").focus();
      fieldInError.first().parent().siblings("select").focus();
      fieldInError.first().parent().siblings("div").children("input").focus();
      fieldInError.first().parent().siblings("div").children("select").focus();

   } else {
      $("input[id='${focusFieldId}']").focus();
      $("select[id='${focusFieldId}']").focus();
   }
   });

</script>