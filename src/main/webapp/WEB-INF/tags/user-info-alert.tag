<%@tag description="Logged In User Info Alert" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="cucreek" tagdir="/WEB-INF/tags"%>

<%-- Display a user info alert in the upper right corner --%>

<%@ attribute name="message" required="true" %>
<%@ attribute name="type" required="true" %> <%-- alert, info, success --%>
<%@ attribute name="id" required="true" %> <%-- div id locator --%>
<%@ attribute name="delay"%>
<%@ attribute name="location"%> <%-- top-right, bottom-right, top-left, bottom-left --%>

<script type="text/javascript">
   $(function () {
      $('#${id}').notify({
         message: {
            html:
                  '${message}'
         },
         fadeOut: { enabled:true, delay: ${empty delay?2500:delay} },
         type: '${type}'
      }).show()
   });
</script>
<div id="${id}" class="notifications ${empty location?'top-right':location}"></div>