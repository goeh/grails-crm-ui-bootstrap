<%@ page defaultCodec="html" %>
<div class="control-group${invalid ? ' error' : ''}${required ? ' required' : ''}">
	<label class="control-label" for="${property}">${label}</label>
    <div class="controls">
		<%= widget %>
	</div>
</div>