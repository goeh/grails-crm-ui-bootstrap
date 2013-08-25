<%@ page import="grails.plugins.crm.core.TenantUtils; grails.util.GrailsNameUtils;" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <r:require modules="application, crm"/>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <r:script>
        $(document).ajaxError(function(e, xhr, settings, exception) {

            var message = '';

            if (xhr.status == 0) {
                message = 'You are offline!\n Please check your network.';
            } else if (xhr.status == 403) {
                window.location.href = "${createLink(mapping: 'start', absolute: true)}";
                return;
            } else if (xhr.status == 404) {
                message = 'Requested URL not found.';
            } else if (xhr.status == 500) {
                message = xhr.responseText;
            } else if (errStatus == 'parsererror') {
                message = 'Error.\nParsing JSON Request failed.';
            } else if (errStatus == 'timeout') {
                message = 'Request timed out.\nPlease try later';
            } else {
                message = ('Unknown Error.\n' + xhr.responseText);
            }

            alert(message);
        });
    </r:script>

    <r:layoutResources/>
    <g:layoutHead/>
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
          <script src="${resource(dir:'js', file: 'html5.js')}"></script>
    <![endif]-->
</head>

<body class="${controllerName ?: 'home'}-body">

<div class="container-fluid">

    <g:pageProperty name="page.hero"/>

    <div class="controller-${controllerName ?: 'home'} action-${actionName ?: 'index'}" id="content-wrapper"
         role="main">
        <g:layoutBody/>
    </div>
</div>

<r:layoutResources/>

</body>
</html>
