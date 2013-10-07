<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<%-- Tag lib to display to the user that form has errors - this is useful for large forms where the error
   may be at the bottom of the form.
   --%>
<%@ attribute name="binding" required="true" %>
<%@ attribute name="showErrors"%>


<s:hasBindErrors name="${binding}">
   <c:set var="pageHasErrors" value="true"></c:set>
</s:hasBindErrors>

<%-- This is handy for debugging validations errors that are in the bound object but are not showing
on the page --%>
<c:if test="${not empty pageHasErrors}">
   <c:if test="${not empty showErrors}">
      <s:hasBindErrors name="${binding}">
         <c:forEach items="${errors.fieldErrors}" var="fieldError">
            ${fieldError.objectName}.${fieldError.field} ${fieldError.rejectedValue} ${fieldError.defaultMessage}
         </c:forEach>
      </s:hasBindErrors>
   </c:if>
   <style type="text/css">
      .notifications.top-right {
         right: 220px;
         top: -4px;
         min-width: 300px;
      }
   </style>
   <script type="text/javascript">
      $(function () {
         $('#${currentPage}_formMessages').notify({
            message: {
               html:
                     '<span class="glyphicon glyphicon-exclamation-sign"></span>&nbsp;'+
                           '<span>'+
                           '<s:message code="global_page_error"/>'+
                           '</span>'
            },
            fadeOut: { enabled:true, delay: 120000 },
            type: 'danger'
         }).show()
      });
   </script>
   <div id="${currentPage}_formMessages" class="notifications top-right"></div>
</c:if>


