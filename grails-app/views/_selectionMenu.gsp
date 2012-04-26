<crm:submenu group="selection" title="${message(code:'default.selections.menu.label')}">
    <g:if test="${selection}">
        <li>
            <g:link controller="selectionRepository" action="create"
                    params="${[location:controllerName, username:crm.user([:], {username}), uri:select.encode(selection:selection), referer:referer ?: request.forwardURI]}">
                <i class="icon-download"></i>
                <g:message
                        code="selectionRepository.save.label" default="Save Selection"/></g:link>
        </li>
    </g:if>
    <select:listRepo location="${location ?: controllerName}" username="${crm.user([:], {username}).toString()}" var="sel">
        <li class="selection-repo">
            <g:link controller="selectionRepository" action="delete" id="${sel.id}"
                    class="pull-right delete" style="display:none;"
                    onclick="return confirm('${message(code:'default.button.delete.confirm.message', default:'Are you sure?')}')">
                <i class="icon-trash"></i></g:link>
            <g:link controller="${location ?: controllerName}" action="${action ?: 'list'}"
                    id="${select.encode(selection:sel.uri)}">
                <i class="icon-search"></i> ${sel.name.encodeAsHTML()}
            </g:link>
        </li>
    </select:listRepo>
</crm:submenu>