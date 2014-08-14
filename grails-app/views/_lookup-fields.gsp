<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="propertyName" value="${grails.util.GrailsNameUtils.getPropertyName(bean.class)}"/>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.name.label')}
    </label>

    <div class="controls">
        <g:textField name="name" value="${bean.name}" autofocus=""/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.description.label')}
    </label>

    <div class="controls">
        <g:textField name="description" value="${bean.description}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.param.label')}
    </label>

    <div class="controls">
        <g:textField name="param" value="${bean.param}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.icon.label')}
    </label>

    <div class="controls">
        <g:textField name="icon" value="${bean.icon}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.orderIndex.label')}
    </label>

    <div class="controls">
        <g:textField name="orderIndex" value="${bean.orderIndex}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label">
        ${message(code: propertyName + '.enabled.label')}
    </label>

    <div class="controls">
        <g:checkBox name="enabled" value="true" checked="${bean.enabled}"/>
    </div>
</div>