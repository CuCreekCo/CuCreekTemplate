<%@tag description="Control Group User Input Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<%--
   Presents an input box wrapped and ready for validation messages, help, and help box.

   id - gets converted to the spring binding path
   binding - is the spring binding object
   labelPrefix - used to lookup field in the messages.properties file
   inputType - default is 'text' - can be 'date' or 'time'
   readOnly - render this field read only
   suppressHelp - do not show the help block or button
   excludeField - when searching for the message in  messages.properties exclude this
      string in the lookup value.  I.e. passField_headerFields.passStyle can exclude
      the "headerFields" so that the same message could be used for primaryFields or:
         passField.passStyle
   optional - display an (Optional) tag in the block help
   cssStyle - a pass through style to the Spring widget
   prepend - a string value to prepend to the input box - only valid for text inputs
   showHelpWhenReadOnly - allow the help icons to show in read only mode
   stripToNumeric - will strip any non-numeric characters from the field
--%>

<%@ attribute name="id" required="true" %>
<%@ attribute name="binding" required="true" %>
<%@ attribute name="labelPrefix" required="true" %>
<%@ attribute name="inputType" %>
<%@ attribute name="readOnly" %>
<%@ attribute name="suppressHelp" %>
<%@ attribute name="excludeField" %>
<%@ attribute name="optional" %>
<%@ attribute name="cssStyle" %>
<%@ attribute name="cssClass" %>
<%@ attribute name="prepend" %>
<%@ attribute name="showHelpWhenReadOnly" %>
<%@ attribute name="stripToNumeric" %>

<cucreek:group-labels id="${id}" labelPrefix="${labelPrefix}"
                  excludeField="${excludeField}" optional="${optional}"/>

<s:bind path="${binding}.${id}"> <!-- sets a status variable with BindStatus fields -->
   <c:set var="bindErrors" value="${status.errorCode}"/>
   <c:set var="bindErorsPath" value="${status.path}"/>
</s:bind>
<div class="form-group ${not empty bindErrors?'has-error':''}" id="${normalizedId}ControlGroupDiv">
   <label class="control-label" id="${normalizedId}ControlLabel" for="${normalizedId}">${label}</label>

   <!-- Strip out non numerics -->
   <c:if test="${not empty stripToNumeric}">
      <script type="text/javascript">
         $(function(){
            $("input[id='${normalizedId}']").on('blur',function(event){
               var _thisValue = $(this).val().replace(/[^0-9.]/g,"");
                  $(this).val(_thisValue);
            });
         });
      </script>
   </c:if>

   <c:choose>
      <c:when test="${inputType eq 'date'}">
         <div class="input-group date" data-date="" data-date-format="mm/dd/yyyy"
              id="${normalizedId}DatePickerDiv">
            <c:choose>
               <c:when test="${not empty readOnly}">
                  <sf:input type="text" id="${normalizedId}" placeholder="${label}"
                            path="${id}" cssClass="form-control input-group-sm" readonly="true" cssStyle="${cssStyle}"/>
               </c:when>
               <c:otherwise>
                  <sf:input type="text" id="${normalizedId}"
                            placeholder="${label}"
                            path="${id}" cssClass="form-control input-group-sm" cssStyle="${cssStyle}"/>
                  <span class="input-group-addon">
                     <span class="glyphicon glyphicon-calendar"></span>
                  </span>
                  <script type="text/javascript">
                     $(function () {
                        var __initialDateValue = $("input[id='${id}']").val();
                        $("div[id='${normalizedId}DatePickerDiv']").attr('data-date',__initialDateValue);
                        $('#${normalizedId}DatePickerDiv').datepicker();
                     });
                  </script>

               </c:otherwise>
            </c:choose>
         </div>
      </c:when>
      <c:when test="${inputType eq 'time'}">
         <div class="input-group time" id="${normalizedId}TimePickerDiv">
            <c:choose>
               <c:when test="${not empty readOnly}">
                  <sf:input type="text" id="${normalizedId}" placeholder="${label}"
                            path="${id}" cssClass="form-control input-group-sm" readonly="true" cssStyle="${cssStyle}"/>
               </c:when>
               <c:otherwise>
                  <sf:input type="text" id="${normalizedId}" placeholder="${label}"
                            path="${id}" cssClass="form-control input-group-sm" cssStyle="${cssStyle}"/>
               <span class="input-group-addon"
                     id="${normalizedId}TimePicker">
                  <span class="glyphicon glyphicon-time"></span></span>
               </c:otherwise>
            </c:choose>
            <script type="text/javascript">
               $('#${normalizedId}').timepicker(
                     {
                        defaultTime: false
                     }
               );
            </script>
         </div>
      </c:when>
      <c:when test="${inputType eq 'textarea'}">
         <c:choose>
            <c:when test="${not empty readOnly}">
               <sf:textarea path="${id}" cssClass="form-control" readonly="true" id="${normalizedId}"
                            cssStyle="${cssStyle}"/>
            </c:when>
            <c:otherwise>
               <sf:textarea path="${id}" cssClass="form-control" placeholder="${label}" id="${normalizedId}"
                            cssStyle="${cssStyle}"/>
            </c:otherwise>
         </c:choose>
      </c:when>
      <c:otherwise>
         <c:choose>
            <c:when test="${not empty readOnly}">
               <c:if test="${not empty prepend}">
                  <div class="input-group">
                  <span class="input-group-addon">${prepend}</span>
               </c:if>
               <sf:input type="${empty inputType?'text':inputType}" id="${normalizedId}"
                         path="${id}" cssClass="form-control ${cssClass}"
                         readonly="true" cssStyle="${cssStyle}"/>
               <c:if test="${not empty prepend}">
                  </div>
               </c:if>

            </c:when>
            <c:otherwise>
               <c:if test="${not empty prepend}">
                  <div class="input-group">
                  <span class="input-group-addon">${prepend}</span>
               </c:if>
               <sf:input type="${empty inputType?'text':inputType}"
                         id="${normalizedId}" placeholder="${label}" path="${id}" cssStyle="${cssStyle}"
                         cssClass="form-control ${cssClass}"/>
               <c:if test="${not empty prepend}">
                  </div>
               </c:if>
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
   <c:if test="${not empty showHelpWhenReadOnly || (not empty helpText && empty readOnly && empty suppressHelp)}">
      <span class="help-block">
         <cucreek:popover id="${normalizedId}Popover" text="${helpText}"
                      title="${helpTitle}"/>
         ${helpBlockText}
      </span>
   </c:if>
</div>
