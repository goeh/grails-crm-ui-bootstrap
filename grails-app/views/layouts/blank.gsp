<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <r:require modules="bootstrap"/>

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114"
          href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <r:layoutResources/>
    <g:layoutHead/>

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>

<body class="${controllerName ?: 'home'}-body">

<div class="container-fluid">

    <g:if test="${flash.info || flash.message}">
        <p class="alert alert-info" style="margin: 10px;" role="status">${flash.info ?: flash.message}</p>
    </g:if>
    <g:if test="${flash.success}">
        <p class="alert alert-success" style="margin: 10px;" role="status">${flash.success}</p>
    </g:if>
    <g:if test="${flash.error}">
        <p class="alert alert-error" style="margin: 10px;" role="status">${flash.error}</p>
    </g:if>

    <g:layoutBody/>
</div>

<r:layoutResources/>

</body>
</html>