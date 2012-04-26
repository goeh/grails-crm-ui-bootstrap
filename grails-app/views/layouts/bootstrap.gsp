<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails CRM"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <r:require modules="bootstrap,crmUiBootstrap"/>
    <g:layoutHead/>
    <r:layoutResources/>
    <link href="${resource(dir: 'css', file: 'crm.css')}" type="text/css" rel="stylesheet" media="screen, projection"/>
    <r:script type="text/javascript">
        /* pre-populate an input field with a default text,
         * then clear the default text when field gets focus.
         */
        $(document).ready(function() {
            $("input.default-text").each(function(i, elem) {
                var input = $(elem);
                var text = input.attr("title");
                input.focus(function() {
                    var value = input.val();
                    if (value == text) {
                        input.val("");
                    }
                });
                input.blur(function() {
                    var value = input.val();
                    if (value == "") {
                        input.val(text);
                    }
                });
                if (input.val() == "") {
                    input.val(text);
                }
            });

            // Put cursor in first enabled input field.
            $('#content form :input:visible:enabled:first').focus();
        });
    </r:script>
    <style type="text/css">
    .topbar .brand {
        padding-top: 4px;
        padding-bottom: 2px;
    }
    </style>
</head>

<body>

<!-- Topbar
    ================================================== -->
<div class="topbar" data-scrollspy="scrollspy">
    <div class="topbar-inner">
        <div class="container-fluid">
            <a class="brand" href="http://grails.org"><img src="${resource(dir: 'images', file: 'grails_logo.png')}"
                                                           alt="Grails" height="30px;"/></a>
            <ul class="nav">
                <li class="active"><a href="#overview">Overview</a></li>
                <li><g:link controller="crmCompany" action="index">Company</g:link></li>
                <li><g:link controller="crmPerson" action="index">Person</g:link></li>
                <li><g:link controller="crmAdmin" action="index">Admin</g:link></li>
            </ul>

            <crm:user>
                <ul class="nav secondary-nav">
                    <li class="dropdown" data-dropdown="dropdown"><a href="#"
                                                                     class="dropdown-toggle">${name.encodeAsHTML()}</a>
                        <ul class="dropdown-menu">
                            <li><g:link controller="account" action="edit">Settings (${grails.plugins.crm.core.TenantUtils.tenant.toString()})</g:link></li>
                            <li><g:link controller="account" action="index">Accounts</g:link></li>
                            <li class="divider"></li>
                            <li><g:link controller="crmAccount" action="logout">Logout</g:link></li>
                        </ul>
                    </li>
                </ul>
            </crm:user>
        </div>
    </div>
</div>

<header class="jumbotron masthead" id="overview">
    <div class="inner">
        <div class="container">
            <h1>Grails CRM</h1>

            <p class="lead">
                Grails <abbr title="Customer Relationship Management">CRM</abbr> is an open source <abbr
                    title="Customer Relationship Management">CRM</abbr> toolbox.<br/>
                Install only the plugins you need and develop your custom <abbr
                    title="Customer Relationship Management">CRM</abbr> application in a few hours.<br/>
            </p>

            <p><strong>Nerd alert:</strong> Grails CRM is <a href="#technipelago"
                                                             title="Technipelago AB">developed by Technipelago AB, Sweden.</a> and was designed to work out of the gate with modern browsers in mind.
            </p>
        </div>
    </div>
</header>

<g:if test="${flash.error}">
    <div class="container" style="padding-top: 40px;">
        <div class="alert-message block-message error">
            ${message(code: flash.error, 'default': flash.defaultMessage ?: flash.error, args: flash.args)}
        </div>
    </div>
</g:if>

<g:if test="${flash.message}">
    <div class="container" style="padding-top: 40px;">
        <div class="alert-message block-message success">
            ${message(code: flash.message, 'default': flash.defaultMessage ?: flash.message, args: flash.args)}
        </div>
    </div>
</g:if>

<div id="content" class="container" style="padding-top: 40px;">
    <g:layoutBody/>
</div>

<footer class="footer" role="contentinfo">
    <div class="container">
        <p>Copyright &copy; 2012 <a href="http://www.technipelago.se" target="_blank">Technipelago AB</a></p>
    </div>
</footer>

<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
<g:javascript library="application"/>
<r:layoutResources/>
</body>
</html>