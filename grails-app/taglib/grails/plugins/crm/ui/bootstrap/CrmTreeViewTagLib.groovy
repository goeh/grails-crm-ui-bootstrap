/*
 * Copyright 2014 Goran Ehrsson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
