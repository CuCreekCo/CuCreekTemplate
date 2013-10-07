<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<%-- Tag to display messages from a validation message put on the MVC model - this is different than
   a message bound to model attribute --%>

<c:if test="${ not empty validationMessageVO }">
   <c:choose>
      <c:when test="${ validationMessageVO.status eq 'ERROR'  }">
         <c:set var="type" value="error"/>
         <c:set var="delay" value="120000"/>
      </c:when>
      <c:when test="${ validationMessageVO.status eq 'SUCCESS'  }">
         <c:set var="type" value="success"/>
         <c:set var="delay" value="4500"/>
      </c:when>
      <c:otherwise>
         <c:set var="type" value="info"/>
         <c:set var="delay" value="7000"/>
      </c:otherwise>
   </c:choose>
   <style type="text/css">
      .notifications.top-right {
         right: 220px;
         top: -4px;
         min-width: 300px;
      }
   </style>
   <script type="text/javascript">
      $(function () {

         $('#${currentPage}_validationMessages').notify({
            message: {
               html:
                     "<i class='icon-exclamation-sign'></i>&nbsp;<span>${ validationMessageVO.message }</span>"

            },
            fadeOut: { enabled: true, delay: ${delay} },
            type: '${type}'
         }).show()
      });
   </script>
   <div id="${currentPage}_validationMessages" class="notifications top-right"></div>

</c:if>