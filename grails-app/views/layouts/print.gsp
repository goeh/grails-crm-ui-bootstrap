<!DOCTYPE html>
<html>
  <head>
    <g:set var="entityName" value="${message(code: controllerName + '.label', default: controllerName)}" />
    <title><g:layoutTitle default="${message(code:'application.name.1', default:'Grails')} ${message(code:'application.name.2', default:'CRM')}"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <link rel="apple-touch-icon" href="${resource(dir:'images',file:'apple-touch-icon.png')}" />
    <bp:blueprintCss/>
    <jqui:resources themeCss="${resource(dir:'css/start',file:'jquery-ui-custom.css', plugin:'crm-base')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'print.css', plugin:'crm-base')}" media="print"/>
    <g:layoutHead />
  </head>
  <body>
    <g:layoutBody />
    <script type="text/javascript">
      window.print();
    </script>
  </body>
</html>
