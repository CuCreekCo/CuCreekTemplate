<%@tag description="Control Group User Input Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<%--
   id is the the path or unique id of the bound object
   binding is the Spring bound object
   labelPrefix is used to lookup labels, help, and help blocks
   minimumSearchLength is the minimum characters entered before starting a search
   readOnly indicates the input is ready only
   suppressHelp will not output any help
   dataUrl - where does the data come from - data must be a CodeTableDTO list
   selectedItemCallback - javascript call back when item selected - the CodeTableDTO object is passed
   notFoundCallback javascript call back when not found - the user input is passed back
   appendCodeToDescription - append the CodeTableDTO.codeValue to the description when displaying the list
   excludeField - exclude part of the field name when looking up the labels in the messages.properties file
                  this is useful when the field is in a collection (fieldName[0].someAttribute]
   autofocus - auto focus on the field at page load

--%>
<%@ attribute name="id" required="true" %>
<%@ attribute name="binding" required="true" %>
<%@ attribute name="labelPrefix" required="true" %>
<%@ attribute name="minimumSearchLength" required="true" type="java.lang.Integer" %>
<%@ attribute name="readOnly" %>
<%@ attribute name="suppressHelp" %>
<%@ attribute name="dataUrl" %>
<%@ attribute name="selectItemCallback" %>
<%@ attribute name="notFoundCallback" %>
<%@ attribute name="appendCodeToDescription" %>
<%@ attribute name="cssClass" %>
<%@ attribute name="excludeField" %>
<%@ attribute name="autofocus" %>


<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                      excludeField="${excludeField}" optional="${optional}"/>

<s:url value="/assets/images/loading.gif" var="loadingGifUrl"/>

<s:bind path="${binding}.${id}"> <!-- sets a status variable with BindStatus fields -->
   <c:set var="bindErrors" value="${status.errorCode}"/>
</s:bind>

<script type="text/javascript">
   $(document).ready(function () {
      var ${normalizedId}_autocomplete_hit_count = 0;
      var ${normalizedId}_autocomplete_hit_content = null;

      $("#${normalizedId}_searchInProgress").hide();

      var codeVal = $("#${id}").val();

      if (codeVal !== '' && codeVal !== null) {
         $("#${normalizedId}_autocomplete_input").val(codeVal);
      }

      /*
       Helper to select a value from the code list
       */
      function selectItem(ui) {
         var thisItem;

         if (ui.item === undefined) {
            thisItem = ui[0];
         }
         else {
            thisItem = ui.item;
         }
         $("#${escapedId}").val(thisItem.codeValue);
         $("#${normalizedId}_autocomplete_input").val(thisItem.codeValue);
      }

      $("#${normalizedId}_autocomplete_input").autocomplete({
         source: "${dataUrl}",
         delay: 0,
         minLength: ${minimumSearchLength},
         change: function (event, ui) {

            console.log('change.  hitcount:' + ${normalizedId}_autocomplete_hit_count)
            if (${normalizedId}_autocomplete_hit_count <= 0) {
               $("#${escapedId}").val('');

               <c:if test="${not empty notFoundCallback}">
               ${notFoundCallback}($("#${normalizedId}_autocomplete_input").val());
               </c:if>
            }
            else {
               if (ui.item === null) //prevent quick type that hits then quick tab out without selecting a value
               {
                  if (${normalizedId}_autocomplete_hit_count === 1) {
                     selectItem(${normalizedId}_autocomplete_hit_content);
                  }
                  else {
                     alert("Please select a value from the ${label} list.");
                     $("#${escapedId}").val('');
                     $("#${normalizedId}_autocomplete_input").val('');
                     $("#${normalizedId}_autocomplete_input").focus();
                  }
               }
            }
         },
         focus: function (event, ui) {

            console.log('focus.  ui:' + ui);

            $("#${normalizedId}_autocomplete_input").val(ui.item.codeValue);
            return false;
         },
         search: function (event, ui) {
            console.log('search.  ui:' + ui);

            $("#${normalizedId}_searchInProgress").show();
         },
         select: function (event, ui) {
            console.log('select.  ui:' + ui);

            selectItem(ui);
            <c:if test="${not empty selectItemCallback}">
            ${selectItemCallback}(ui.item);
            </c:if>
            return false;
         },
         response: function (event, ui) {
            console.log('response. ui.content.length:' + ui.content.length);

            ${normalizedId}_autocomplete_hit_count = ui.content.length;
            ${normalizedId}_autocomplete_hit_content = ui.content;

            $("#${normalizedId}_searchInProgress").hide();
            /* Check for redirect to log on */
            if (ui.content.indexOf('j_spring_security_check') >= 0) {
               window.location.reload();
            }
         }
      }).data("ui-autocomplete")._renderItem = function (ul, item) {
         <c:choose>
         <c:when test="${not empty appendCodeToDescription}">
         return $("<li>")
               .append("<a>" + item.codeValue + " - " + item.description + "</a>")
               .appendTo(ul);
         </c:when>
         <c:otherwise>
         return $("<li>")
               .append("<a>" + item.description + "</a>")
               .appendTo(ul);
         </c:otherwise>
         </c:choose>
      };

   });
</script>

<div class="form-group ${not empty bindErrors?'has-error':''}" id="${normalizedId}ControlGroupDiv">

   <label class="control-label" id="${normalizedId}ControlLabel" for="${id}">${label}</label>
   <c:choose>
      <c:when test="${not empty readOnly}">
         <sf:input type="${empty inputType?'text':inputType}"
                   path="${id}" id="${normalizedId}"
                   readonly="true" class="form-control"/>
         <input type="hidden" id="${normalizedId}_autocomplete_input" name="${normalizedId}_autocomplete_input}"/>
      </c:when>
      <c:otherwise>
         <div>
            <sf:hidden path="${id}"/>
            <c:choose>
               <c:when test="${not empty autofocus}">
                  <input type="text" autofocus="true" id="${normalizedId}_autocomplete_input"
                         name="${normalizedId}_autocomplete_input"
                         class="form-control ${cssClass}"/>
               </c:when>
               <c:otherwise>
                  <input type="text" id="${normalizedId}_autocomplete_input"
                         name="${normalizedId}_autocomplete_input"
                         class="form-control ${cssClass}"/>
               </c:otherwise>
            </c:choose>
            <img src="${loadingGifUrl}"
                 id="${normalizedId}_searchInProgress"/>
         </div>
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