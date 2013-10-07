<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Tag lib to display a popover --%>

<%@ attribute name="id" required="true"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="text" required="true"%>
<%@ attribute name="placement" %>

<span id="${id}" style="cursor: pointer;" class="popover-link"
	rel="popover" data-content="${text}"
	data-original-title="${title}">
		<span class="glyphicon glyphicon-info-sign"></span>
</span>
<script type="text/javascript">
	$(function()
	{
		$("#${id}").popover({
			"animation":true,
			"placement":"${not empty placement?placement : 'bottom'}"
			});

      $('body').on('click', function (e) {
         $('.popover-link').each(function () {
            //the 'is' for buttons that triggers popups
            //the 'has' for icons within a button that triggers a popup
            if (!$(this).is(e.target) &&
                  $(this).has(e.target).length === 0 &&
                  $('.popover').has(e.target).length === 0) {
               $(this).popover('hide');
            }
         });
      });
	});
</script>

