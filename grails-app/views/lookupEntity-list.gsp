<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}"/>
    <title><g:message code="${beanName}.list.title" args="[entityName]"/></title>
    <r:script>
        $(document).ready(function () {
            $(".table-striped tr").hover(function () {
                $("i", $(this)).removeClass('hidden');
            }, function () {
                $("i", $(this)).addClass('hidden');
            });
        });
    </r:script>
</head>

<body>

<crm:header title="${beanName}.list.title" args="[entityName, totalCount]"
            subtitle="${beanName}.totalCount.label"/>

<div class="row-fluid">

    <div class="span9">
        <table class="table table-striped">
            <thead>
            <tr>
                <crm:sortableColumn property="name"
                                    title="${message(code: beanName + '.name.label', default: 'Name')}"/>
                <crm:sortableColumn property="orderIndex"
                                    title="${message(code: beanName + '.orderIndex.label', default: 'Sort')}"/>
                <crm:sortableColumn property="param"
                                    title="${message(code: beanName + '.param.label', default: 'Parameter')}"/>
                <crm:sortableColumn property="icon"
                                    title="${message(code: beanName + '.icon.label', default: 'Icon')}"/>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${result}" status="i" var="m">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                    <td><g:link action="edit" id="${m.id}">${fieldValue(bean: m, field: "name")}</g:link></td>
                    <td>${fieldValue(bean: m, field: "orderIndex")}</td>
                    <td>${fieldValue(bean: m, field: "param")}</td>
                    <td>${fieldValue(bean: m, field: "icon")}
                        <g:if test="${m.icon}">
                            <span class="icon"><img width="16" height="16" src="${fam.icon(name: m.icon)}" alt="icon"/>
                            </span>
                        </g:if>
                    </td>
                    <td style="width:40px;">
                        <g:link action="moveUp" id="${m.id}"><i class="icon icon-arrow-up hidden"></i></g:link>
                        <g:link action="moveDown" id="${m.id}"><i class="icon icon-arrow-down hidden"></i></g:link>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <crm:paginate total="${totalCount}"/>

        <div class="form-actions btn-toolbar">
            <g:form action="index">
                <g:if test="${totalCount}">
                    <input type="hidden" name="offset" value="${params.offset ?: ''}"/>
                    <input type="hidden" name="max" value="${params.max ?: ''}"/>
                    <input type="hidden" name="sort" value="${params.sort ?: ''}"/>
                    <input type="hidden" name="order" value="${params.order ?: ''}"/>
                    <input type="hidden" name="format" value="xls"/>
                    <crm:button visual="primary" icon="icon-print icon-white"
                                label="${beanName}.button.export.label"/>
                </g:if>
                <crm:button type="link" action="create" visual="success" icon="icon-file icon-white"
                            label="${beanName}.button.create.label" permission="${beanName}:create" accesskey="n"/>
            </g:form>
        </div>
    </div>

    <div class="span3">
        <crm:submenu/>
    </div>
</div>

</body>
</html>
