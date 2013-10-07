<%@tag description="Confirmation Dialog" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Confirmation Dialog
      Display a modal dialog that prompts the user to cancel an action or continue with the action.
      The resulting action requires a callback JavaScript function or simply hides the dialog and takes
      no action.

   modalId - html ID of this modal on the page
   modalHeaderMessage - header displayed in the modal
   modalBodyMessage - body of the modal
   confirmationCallback - callback JavaScript on confirmation
   cancelCallback - callback on cancel (optional)
   confirmButtonText - text for the confirm button
   --%>

<%@ attribute name="modalId" required="true"%>
<%@ attribute name="modalHeaderMessage" required="true"%>
<%@ attribute name="modalBodyMessage" required="true"%>
<%@ attribute name="confirmationCallback" required="true"%>
<%@ attribute name="cancelCallback"%>
<%@ attribute name="hideCloseIcon"%>
<%@ attribute name="cancelButtonText"%>
<%@ attribute name="confirmButtonText"%>

<script type="text/javascript">
$(document).ready(function(){

	$("#${modalId} #continueButton").on("click",
	   function(event)
	   {
         event.preventDefault();
         ${confirmationCallback}(event);
	   }
	);

   <c:if test="${not empty cancelCallback}">
      $("#${modalId} #cancelButton").on("click",
            function(event)
            {
               event.preventDefault();
               ${cancelCallback}(event);
            }
      );
   </c:if>
});
</script>
<div id="${ modalId }" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="${ modalId }Label"
     aria-hidden="true" data-backdrop="static" data-keyboard="false">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <c:if test="${empty hideCloseIcon}">
               <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></button>
            </c:if>
            <h3 id="${ modalId }Label"><i class="icon-warning-sign"></i>&nbsp;${ modalHeaderMessage }</h3>
         </div>
         <div class="modal-body">
            <p>${ modalBodyMessage }</p>
         </div>
         <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn-red"
                    id="cancelButton">${empty cancelButtonText?'No':cancelButtonText}</button>
            <button type="button" class="btn btn-green"
                    id="continueButton">${empty confirmButtonText?'Yes':confirmButtonText}</button>
         </div>
      </div>
   </div>
</div>
