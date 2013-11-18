package grails.plugins.crm.ui.bootstrap

/**
 * Created by goran on 2013-11-14.
 */
class CrmTreeViewTagLib {

    static namespace = "crm"

    def treeview = { attrs, body ->

        out << """<div class="${attrs['class'] ?: 'crm-treeview'}">"""

        def root = attrs.node
        if (root) {
            renderChildNodes(root, out)
        }

        out << "</div>"
    }

    private void renderChildNodes(final Node parent, final Writer out) {

        out << "<ul>"

        for (Node node in parent.children()) {
            def id = node.attribute('id')
            out << "<li>"
            if (node.attribute('type') == 'folder') {
                out << """<input type="checkbox" id="$id" ${node.attribute('open') ? 'checked="checked"' : ''}/><label for="$id">${node.name()}</label>"""
                renderChildNodes(node, out)
            } else {
                out << """<a href="${node.attribute('href') ?: '#'}" id="$id">${node.name()}</a>"""
            }
            out << "</li>"
        }

        out << "</ul>"
    }
}
