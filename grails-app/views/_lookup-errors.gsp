<g:hasErrors bean="${bean}">
    <crm:alert class="alert-error">
        <ul>
            <g:eachError bean="${bean}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </crm:alert>
</g:hasErrors>