<%@tag description="cucreek DFS Control Group Searchable Input Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>

<%--
   Search Input Group is a search text box with a search button
   and clear button affixed.

   The input search box has the following attributes:

      id - object attribute to search by
      binding - the object where the id attribute lives
      labelPrefix - label prefix contained in messages.properties is used
            to fill in the label and help fields
      inputType - can be date or text
      readOnly - set the input box to read only
      suppressHelp - don't display the help icon
      searchUrl - the endpoint that performs the search
      additionalSearchField - ability to add another 'id' from the
            underyling binding object.  Ala driver's license number and
            state.
      searchDoneCallback - javascript method to call when the search is done
      searchErrorCallback - javascript method to call when the search
            encounters an error
      cssClass - class to style the input box.
      includeClearButton - show or hide the clear button
      clearCallback - javascript method to call on clear
      searchOnBlurField - when this field is blurred, perform the search
      hideSearchButton - hides the search button.  Presume searching is done vial the searchOnBlurField
--%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="binding" required="true"%>
<%@ attribute name="labelPrefix" required="true"%>
<%@ attribute name="inputType"%>
<%@ attribute name="readOnly"%>
<%@ attribute name="suppressHelp"%>
<%@ attribute name="searchUrl"%>
<%@ attribute name="additionalSearchField" %>

<%@ attribute name="hideSearchButton" %>
<%@ attribute name="searchOnBlurField"%>
<%@ attribute name="searchDoneCallback"%>
<%@ attribute name="searchErrorCallback"%>
<%@ attribute name="cssClass"%>
<%@ attribute name="includeClearButton"%>
<%@ attribute name="clearCallback"%>

<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                  excludeField="${excludeField}" optional="${optional}"/>

<s:url value="/assets/images/loading.gif" var="loadingGifUrl"/>

<s:bind path="${binding}.${id}"> <!-- sets a status variable with BindStatus fields -->
	<c:set var="bindErrors" value="${status.errorCode}"/>
</s:bind>

<script type="text/javascript">
   /* This method is called when the search button is pressed or the searchOnBlurField exists
      and is blurred.
    */
   function performSearchInputSearch(event)
   {
      $("#${normalizedId}_searchInProgress").show();

      var searchValue = $("input[id='${id}']").val();

      <c:if test="${not empty additionalSearchField}">
      var additionalSearchValue = $("input[id='${additionalSearchField}']").val();
      if (additionalSearchValue==null || additionalSearchValue==undefined){
         additionalSearchValue = $("select[id='${additionalSearchField}']").val();
      }
      if(additionalSearchValue==undefined){
         additionalSearchValue=null;
      }
      searchValue += '/'+additionalSearchValue;
      </c:if>
      if (searchValue!=null)
      {
         console.log(searchValue);
         var jqxhr = $.ajax(
               "${searchUrl}"+"/"+searchValue,
               {
                  "dataType": 'json',
                  "type": "GET",
                  "jsonp": false
               });

         jqxhr.fail(function (response)
         {
            $("#${normalizedId}_searchInProgress").hide();

            /* Check for redirect to log on */
            if (response.responseText.indexOf('j_spring_security_check') >=0 )
            {
               window.location.reload();
            }
            <c:if test="${not empty searchErrorCallback}">
            ${searchErrorCallback}(response);
            </c:if>
         });
         jqxhr.done(function (response) {
            $("#${normalizedId}_searchInProgress").hide();
            <c:if test="${not empty searchDoneCallback}">
            ${searchDoneCallback}(response);
            </c:if>
         });
      }
   }

   $(function()
   {
      $("#${normalizedId}_searchInProgress").hide();
      $("#${normalizedId}_searchButton").on('click',
            performSearchInputSearch);
      <c:if test="${not empty includeClearButton}">
         $("#${normalizedId}_clearButton").on('click',function(event){
            event.preventDefault();
            ${clearCallback}(event);
         });
      </c:if>
      <c:if test="${not empty searchOnBlurField}">
         $("#${searchOnBlurField}").on('blur',performSearchInputSearch);
      </c:if>
   });
</script>

<div class="form-group ${not empty bindErrors?'has-error':''}" id="${normalizedId}ControlGroupDiv">

	<label class="control-label" for="${normalizedId}">${label}</label>
   <c:choose>
      <c:when test="${inputType eq 'date'}">
         <div class="input-group" data-date="" data-date-format="mm/dd/yyyy" id="${id}">
            <c:choose>
               <c:when test="${not empty readOnly}">
                  <sf:input type="text" id="${normalizedId}" placeholder="${label}" path="${id}"
                            cssClass="${cssClass}" readonly="true"/>
               </c:when>
               <c:otherwise>
                  <sf:input type="text" id="${normalizedId}" placeholder="${label}" path="${id}" cssClass="${cssClass}"/>
                  <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                  <c:if test="${empty hideSearchButton}">
                     <button class="btn" type="button" id="${normalizedId}_searchButton">
                        <span class="glyphicon glyphicon-search"></span>
                     </button>
                  </c:if>
                  <c:if test="${not empty includeClearButton}">
                     <button class="btn" type="button" id="${normalizedId}_clearButton">
                        <span class="glyphicon glyphicon-remove"></span>
                     </button>
                  </c:if>
               </c:otherwise>
            </c:choose>
         </div>
         <script type="text/javascript">
            $("input[id='${id}']").datepicker();
         </script>
      </c:when>
      <c:otherwise>
         <c:choose>
            <c:when test="${not empty readOnly}">
               <sf:input type="${empty inputType?'text':inputType}" id="${normalizedId}"
                         placeholder="${label}" path="${id}" cssClass="${cssClass}"
                         readonly="true"/>
            </c:when>
            <c:otherwise>
               <div class="input-group">
                  <sf:input type="${empty inputType?'text':inputType}" id="${normalizedId}"
                            placeholder="${label}" path="${id}" cssClass="${cssClass}"/>
                  <c:if test="${empty hideSearchButton}">
                     <button class="btn" type="button" id="${normalizedId}_searchButton">
                        <span class="glyphicon glyphicon-search"></span>
                     </button>
                  </c:if>
                  <c:if test="${not empty includeClearButton}">
                     <button class="btn" type="button" id="${normalizedId}_clearButton">
                        <span class="glyphicon glyphicon-remove"></span>
                     </button>
                  </c:if>
                  <img src="${loadingGifUrl}"
                       id="${normalizedId}_searchInProgress"/>

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
