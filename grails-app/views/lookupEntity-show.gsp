<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}"/>
    <title><g:message code="${beanName}.show.title" args="[entityName, bean]"/></title>
</head>

<body>

<crm:header title="${beanName}.show.title" args="[entityName, bean]"/>

<div class="row-fluid">

    <div class="span9">
        <dl>
            <dt><g:message code="${beanName}.name.label"/></dt>
            <dd>${fieldValue(bean: bean, field: 'name')}</dd>

            <dt><g:message code="${beanName}.orderIndex.label"/></dt>
            <dd>${bean.orderIndex}</dd>

            <dt><g:message code="${beanName}.icon.label"/></dt>
            <dd>${fieldValue(bean: bean, field: 'icon')}
                <g:if test="${bean.icon}">
                    <img width="16" height="16" src="${fam.icon(name: bean.icon)}" alt="icon"/>
                </g:if>
            </dd>
            <dt><g:message code="${beanName}.param.label"/></dt>
            <dd>${fieldValue(bean: bean, field: 'param')}</dd>

            <dt><g:message code="${beanName}.enabled.label"/></dt>
            <dd><g:formatBoolean boolean="${bean.enabled}"/></dd>
        </dl>

        <div class="form-actions btn-toolbar">
            <crm:button type="link" visual="primary" action="edit" id="${bean.id}" icon="icon-pencil icon-white"
                        label="${beanName}.button.edit.label"
                        permission="${beanName}:edit" accesskey="s"/>

            <crm:button type="link" group="true" action="create"
                        visual="success"
                        icon="icon-file icon-white"
                        label="${beanName}.button.create.label" permission="${beanName}:create"
                        accesskey="n"/>
        </div>
    </div>

    <div class="span3">
        <crm:submenu/>
    </div>

</div>

</body>
</html>
