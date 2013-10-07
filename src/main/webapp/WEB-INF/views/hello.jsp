<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<cucreek:master>
   <s:url value="/hello" var="cancelUrl"/>
   <s:url value="/hello/list" var="helloListUrl"/>
   <cucreek:messages/>
   <cucreek:focus-errors focusFieldId="helloTypeCode"/>

   <s:url value="/hello/save" var="saveUrl"/>
   <s:url value="/hello/helloTypesAutocomplete" var="helloTypesAutocompleteUrl"/>
   <div class="row">
      <p>Logged on.</p>
   </div>
   <div class="row">
      <p class="lead">Sample Form</p>
      <sf:form id="helloForm" action="${saveUrl}" modelAttribute="helloDTO">

         <cucreek:form-messages binding="helloDTO"/>

         <cucreek:autocomplete id="helloTypeCode" binding="helloDTO"
                               labelPrefix="hello" minimumSearchLength="3"
                               dataUrl="${helloTypesAutocompleteUrl}"/>

         <cucreek:select-group id="userRoleCode" binding="helloDTO"
                               labelPrefix="hello" selectList="${userRoleCodeList}"/>

         <cucreek:input-group id="saySomething" binding="helloDTO" labelPrefix="hello"/>

         <cucreek:input-group id="helloDate" binding="helloDTO" labelPrefix="hello"
                              inputType="date" optional="true"/>

         <button class="btn btn-primary"><s:message code="save_button"/></button>
      </sf:form>
   </div>

   <p></p>

   <div class="row">
      <p class="lead">Sample Table</p>
   </div>

   <s:url value="/assets/js/moment.min.js" var="momentDateUtilsJs"/>
   <script src="${momentDateUtilsJs}" type="text/javascript"></script>

   <cucreek:datagrid-datasource listUrl="${helloListUrl}"/>

   <s:message code="hello_saySomething" var="saySomethingLabel"/>
   <s:message code="hello_helloTypeCode" var="helloTypeCodeLabel"/>
   <s:message code="hello_userRoleCode" var="userRoleCodeLabel"/>
   <s:message code="hello_helloDate" var="helloDateLabel"/>

   <!-- Set up the DataGrid as the contents are pulled via AJAX -->
   <script type="text/javascript">
      $(document).ready(function(){

         $('#MyGrid').datagrid({
            dataSource: new CuCreekDataGridDataSource({
               columns: [
                  {
                     property: 'saySomething',
                     label: '${saySomethingLabel}',
                     sortable: true
                  },
                  {
                     property: 'helloTypeCode',
                     label: '${helloTypeCodeLabel}',
                     sortable: true
                  },
                  {
                     property: 'userRoleCode',
                     label: '${userRoleCode}',
                     sortable: true
                  },
                  {
                     property: 'helloDate',
                     label: '${helloDateLabel}',
                     sortable: false
                  }
               ],
               <%-- If the preSearchParams are present, add them to the search so this page will search on load --%>
               <c:if test="${not empty preSearchParams}">
               search: '${preSearchParams}',
               </c:if>

               /* Format the dates and provide a link
                on the saySomething label
                */
               formatter: function (items) {
                  var searchText = $("#input_search").val();

                  $.each(items, function (index, item) {
                     item.saySomething = '<a href="#">' + item.saySomething + '</a>';
                     if (item.helloTypeCode != null) {
                        item.helloTypeCode = item.helloTypeCode.description;
                     }
                     else {
                        item.helloTypeCode = '';
                     }
                     if (item.userRoleCode != null) {
                        item.userRoleCode = item.userRoleCode.description;
                     }
                     else {
                        item.userRoleCode = '';
                     }
                     if (item.helloDate != null) {
                        item.helloDate = moment(item.helloDate).format("MM/DD/YYYY");
                     }
                  });
               }
            }),
            stretchHeight: false
         });
      });
   </script>
   <div class="row">
      <div class="col-md-12">
         <div class="row">
            <div class="input-group pull-right">
               <a href="${cancelUrl}" class="btn btn-primary" name="cancel">
                  <span class="glyphicon glyphicon-remove"></span> <s:message code="cancel_button"/>
               </a>
            </div>
         </div>
         <div>
            <div style="height:100%;width:100%;margin-bottom:12px;">
               <table id="MyGrid" class="table table-bordered datagrid">
                  <thead>
                  <tr>
                     <th>
                        <span class="datagrid-header-title">
                           <s:message
                                 code="list_label" arguments="Hello"/></span>

                        <div class="datagrid-header-left"></div>
                        <div class="datagrid-header-right">
                           <div class="input-group search">
                              <input type="text" class="form-control"
                                     placeholder="Search"
                                     id="search"/>
                              <button class="btn input-group-addon">
                                 <span class="glyphicon glyphicon-search"></span></button>
                           </div>
                        </div>
                     </th>
                  </tr>
                  </thead>
                  <tfoot>
                  <tr>
                     <th>
                        <div class="datagrid-footer-left" style="display:none;">
                           <div class="grid-controls">
                              <span>
                                 <span class="grid-start"></span> -
                                 <span class="grid-end"></span> of
                                 <span class="grid-count"></span>
                              </span>
                              <select class="grid-pagesize">
                                 <option>25</option>
                                 <option>50</option>
                                 <option>100</option>
                              </select>
                              <span>Per Page</span>
                           </div>
                        </div>
                        <div class="datagrid-footer-right" style="display:none;">
                           <div class="grid-pager form-inline">
                              <button class="btn grid-prevpage">
                                 <span class="glyphicon glyphicon-chevron-left"></span>
                              </button>
                              <span>Page</span>
                              <input type="text" class="form-control" style="width: 50px;">
                              <span>of <span class="grid-pages"></span></span>
                              <button class="btn grid-nextpage">
                                 <span class="glyphicon glyphicon-chevron-right"></span>
                              </button>
                           </div>
                        </div>
                     </th>
                  </tr>
                  </tfoot>
               </table>
            </div>
         </div>
      </div>
   </div>
</cucreek:master>
