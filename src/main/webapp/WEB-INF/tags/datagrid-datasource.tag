<%@tag description="Datagrid JavaScript Object Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags" %>

<%--
 Using the FuelUX Datagrid as the table viewer and build the datagrid
 datasource

 listUrl - the URL that provides the list data in the FuelUX JSON format

 --%>
<%@attribute name="listUrl" required="true" %>

<script type="text/javascript">

  /**
   * build a datagrid select option dropdown or a simple edit item link.
   *
   * @param buildString
   * @param itemId
   * @param itemValue
   * @param selectUrl
   * @param editUrl
   * @return {*|void}
    */
   function buildDataGridSelectEditItem(buildString, itemId, itemValue, selectUrl, editUrl)
   {
      var retString = buildString.replace(/___itemId___/g,itemId);
      retString = retString.replace(/___itemValue___/g, itemValue);
      retString = retString.replace(/___selectUrl___/g, selectUrl);
      retString = retString.replace(/___editUrl___/g, editUrl);
      return retString;

   }

   var CuCreekDataGridDataSource = function (options) {
      this._columns = options.columns;
      this._formatter = options.formatter;
      this._search = options.search;
   };

   CuCreekDataGridDataSource.prototype = {

      /**
       * Returns stored column metadata
       */
      columns: function () {
         return this._columns;
      },

      /**
       * Called when Datagrid needs data. Logic should check the options parameter
       * to determine what data to return, then return data by calling the callback.
       * @param {object} options Options selected in datagrid (ex: {pageIndex:0,pageSize:5,search:'searchterm'})
       * @param {function} callback To be called with the requested data.
       */
      data: function (options, callback) {

         var url = '${listUrl}';
         var self = this;
         var paramChar = '?';
         console.log(options);
         if (options.search != undefined) {
            var match,
                  pl     = /[\+]/g,
                  poundSign = /[#]/g,
                  search = /([^&=]+)=?([^&]*)/g,
                  decode = function (s) {
                     s = decodeURIComponent(s.replace(pl," "));
                     s = s.replace(poundSign,"%23");
                     return s;
                  },
                  query  = options.search;
            var urlParams = {};
            var matchedValidParams = false;
            while (match = search.exec(query)) {
               if(decode(match[2])!=null && decode(match[2]).length>0){
                  urlParams[decode(match[1])] = decode(match[2]);
                  matchedValidParams = true;
               }
            }
            var searchString = '';
            if(!matchedValidParams){ //No & so just a single search
               searchString = decode(options.search);
            } else {
               for(var paramValue in urlParams){
                  searchString+='&'+paramValue+'='+urlParams[paramValue];
               }
            }
            console.log('datagrid-datasource searchString:'+searchString);
            url += paramChar + 'search=' + searchString;
            paramChar = '&';
         }
         else if (this._search!=undefined)
         {
            /*
            This search would be set on page initialization
             */
            url += paramChar + 'search=' + this._search;
            paramChar = '&';
         }
         if (options.sortProperty != undefined) {
            url += paramChar + 'sortProperty=' + options.sortProperty;
            paramChar = '&';

         }
         if (options.sortDirection != undefined) {
            url += paramChar + 'sortDirection=' + options.sortDirection;
            paramChar = '&';

         }
         if (options.pageIndex != undefined) {
            url += paramChar + 'pageIndex=' + options.pageIndex;
            paramChar = '&';

         }
         if (options.pageSize != undefined) {
            url += paramChar + 'pageSize=' + options.pageSize;
         }

         console.log('datagrid url:' + url);

         var jqxhr = $.ajax(url,
               {
                  "dataType": 'json',
                  "type": "GET",
                  "jsonp": false
               });

         jqxhr.error(function (response)
         {
            console.log(response.responseText);
            console.log(response.responseText.indexOf('j_spring_security_check'));

            /* Check for redirect to log on */
            if (response.responseText.indexOf('j_spring_security_check') >=0 )
            {
               window.location.reload();
            }
         });
         jqxhr.done(function (response) {
                  // Prepare data to return to Datagrid
                  var data = response.data;
                  var count = response.count;
                  var end = response.end;
                  var pages = response.pages;
                  var page = response.page;
                  var start = response.start;

                  // Allow client code to format the data
                  if (self._formatter) self._formatter(data);

                  // Return data to Datagrid
                  callback({ data: data, start: start, end: end, count: count, pages: pages, page: page });

               });
      }
   };
</script>