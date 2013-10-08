CuCreekTemplate
===============
The CuCreekTemplate (Copper Creek Software) project is an opinionated Spring 3.2 MVC web application template.  It consists
of a standard Spring MVC project that uses:
* JSP with JSTL for the views
* Spring Security
* Spring @Controllers
* Spring @Repositories with JPA
* Spring @Services as the business
* EclipseLink as the JPA provider
* Bootstrap 3.0 for styling and widgets
* jQuery for all the JavaScript junk
* A handful of custom JSTL-like tags to simplify things like Bootstrap input groups, autocomplete, et al
* Maven to build.

All the current versions and dependencies can be found in the pom.xml file.

The default version uses an embedded HSQL database to get up and running quickly.

How to Start the Web App
========================
Assuming you have Maven installed, just start it up with the embedded Tomcat runner:

`$ mvn tomcat7:run`

Obviously, this assumes you're in the same directory as the _pom.xml_ file when you run this.

Open up your web browser and point it to:  **http://localhost:8080/CuCreek/hello**

You'll probably be asked to log on, use: **jason/12345678**

The hello page shows a quick example of the custom tags and a datagrid.

Everything else is documented in the code/JavaDocs - but just barely and probably poorly.

Other Stuff
===========
Could I have just created a Maven archetype?  Or a Spring Boot script?  Maybe a Spring Roo script? Perhaps
some other wizardry?  Yeah.  But this works for me.

Also, you may have noticed the IntelliJ `CuCreekMVCTemplate.iml` file - I used IDEA 12.

