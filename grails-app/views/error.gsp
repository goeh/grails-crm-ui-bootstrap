<%@ page import="grails.util.Environment;" %><!DOCTYPE html>
<html>
<head>
    <title><g:message code="exception.title" default="Runtime Exception"/></title>
    <meta name="layout" content="blank">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
</head>

<body>

<g:if test="${Environment.developmentMode && exception}">
    <g:renderException exception="${exception}"/>
</g:if>
<g:else>
    <h1><g:message code="exception.title" default="Runtime Exception"/></h1>
    <h2><g:message code="exception.subtitle" default="Please contact technical support."/></h2>
</g:else>

<g:link mapping="start"><g:message code="home.label" default="Home"/></g:link>

</body>
</html>