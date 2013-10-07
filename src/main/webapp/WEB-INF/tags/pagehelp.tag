<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Page help Tag

   Tag to include the page help ajax call.

   url - append a url to the default help request url
   useThisControllerMapping - use this mapping rather than the default help mapping
   --%>
<%@ attribute name="url" %>
<%@ attribute name="useThisControllerMapping" %>

<s:url value="/assets/images/loading.gif" var="loadingGifUrl"/>

<c:choose>
   <c:when test="${not empty useThisControllerMapping}">
      <c:set var="helpControllerMapping"
             value="${useThisControllerMapping}"/>
   </c:when>
   <c:otherwise>
      <c:set var="helpControllerMapping"
             value="${currentPage}/help"/>
   </c:otherwise>
</c:choose>
<c:choose>
   <c:when test="${not empty url}">
      <s:url value="/${helpControllerMapping}${url}" var="helpURL"/>
   </c:when>
   <c:otherwise>
      <s:url value="/${helpControllerMapping}" var="helpURL"/>
   </c:otherwise>
</c:choose>
<div id="pageHelpModal" class="modal hide fade" tabindex="-1" role="dialog"
     aria-labelledby="pageHelpModalLabel"
     aria-hidden="true"
      data-backdrop="false" data-keyboard="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <c:if test="${empty hideCloseIcon}">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i>
               </button>
            </c:if>
            <h3 id="pageHelpModalLabel"><i class="icon-question-sign"></i>&nbsp;Page Help - ${currentPage}</h3>
         </div>
         <div class="modal-body">
            <div class="padded" id="pageHelpModalBody"></div>
         </div>
         <div class="modal-footer">
            <img src="${loadingGifUrl}"
                 id="pageHelpModal_gettingHelpInProgress"/>
         </div>
      </div>
   </div>
</div>
<script type="text/javascript">
   $(document).ready(function () {
      $('a[id="pageHelpLink"]').on("click",
            function (event) {
               $("#pageHelpModal_gettingHelpInProgress").show();

               $.get('${helpURL}',
                     function (data) {

                        $('div[id="pageHelpModalBody"]').html(data);
                        $("#pageHelpModal").modal('show');
                        $("#pageHelpModal_gettingHelpInProgress").hide();

                     });
            });
   });
</script>
