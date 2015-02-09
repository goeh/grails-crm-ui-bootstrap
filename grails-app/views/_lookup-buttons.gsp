<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="propertyName" value="${grails.util.GrailsNameUtils.getPropertyName(bean.class)}"/>

<div class="form-actions">
    <crm:button visual="warning" icon="icon-ok icon-white" label="${propertyName}.button.update.label"/>
    <crm:button action="delete" visual="danger" icon="icon-trash icon-white"
                label="${propertyName}.button.delete.label"
                confirm="${propertyName}.button.delete.confirm.message"
                permission="${propertyName}:delete"/>
    <crm:button type="link" action="list" icon="icon-remove" label="${propertyName}.button.cancel.label"/>
</div>