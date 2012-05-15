<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: beanName + '.label', default: beanName)}"/>
    <title><g:message code="${beanName}.edit.title" args="[entityName, bean]"/></title>
</head>

<body>

<crm:header title="${beanName}.edit.title" args="[entityName, bean]"/>

<div class="row-fluid">

    <div class="span9">

        <g:hasErrors bean="${bean}">
            <bootstrap:alert class="alert-error">
                <ul>
                    <g:eachError bean="${bean}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                error="${error}"/></li>
                    </g:eachError>
                </ul>
            </bootstrap:alert>
        </g:hasErrors>

        <g:form action="save">
            <g:hiddenField name="id" value="${bean?.id}"/>
            <g:hiddenField name="version" value="${bean?.version}"/>

            <f:with bean="${bean}">

                <div class="row-fluid">
                    <div class="span7">
                        <f:field property="name" input-autofocus="" input-class="span4"/>
                        <f:field property="description" input-rows="5" input-class="span4"/>
                    </div>

                    <div class="span5">
                        <f:field property="orderIndex"/>
                        <f:field property="icon"/>
                        <f:field property="param"/>
                        <f:field property="enabled"/>
                    </div>
                </div>

            </f:with>

            <div class="form-actions">
                <crm:button visual="primary" action="edit" icon="icon-ok icon-white"
                            label="${beanName}.button.save.label"/>
                <crm:button visual="danger" action="delete" icon="icon-trash icon-white"
                            label="${beanName}.button.delete.label" formnovalidate=""
                            confirm="${beanName}.button.delete.confirm.message" permission="${beanName}:delete"/>
                <crm:button type="link" action="index" icon="icon-remove" label="${beanName}.button.cancel.label"/>
            </div>

        </g:form>
    </div>

    <div class="span3">
        <crm:submenu/>
    </div>

</div>

</body>
</html>
