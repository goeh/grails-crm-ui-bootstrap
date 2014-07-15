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

import grails.plugins.crm.core.TenantUtils
import javax.servlet.http.HttpServletResponse

abstract class CrmLookupEntityController {

    static allowedMethods = [index: ["GET", "POST"], create: ["GET", "POST"], edit: ["GET", "POST"], delete: "POST"]

    protected boolean checkPermission(bean) {
        return bean?.tenantId == TenantUtils.tenant
    }

    def index() {
        params.offset = params.offset ? params.int('offset') : 0
        params.max = Math.min(params.max ? params.int('max') : 20, 100)
        if (!params.sort) params.sort = 'orderIndex'
        if (!params.order) params.order = 'asc'
        def result = domainClass.createCriteria().list(params) {
            eq('tenantId', TenantUtils.tenant)
        }
        render(view: '/lookupEntity-list', plugin: 'crm-ui-bootstrap', model: [beanName: beanName, result: result, totalCount: result.totalCount])
    }

    def show() {
        def instance = domainClass.get(params.id)
        if (checkPermission(instance)) {
            render(view: '/lookupEntity-show', plugin: 'crm-ui-bootstrap', model: [bean: instance, beanName: beanName])
        } else {
            response.sendError(404) // Not found
        }
    }

    def create() {
        def instance = domainClass.newInstance()
        switch (request.method) {
            case "GET":
                instance.properties = params
                instance.tenantId = TenantUtils.tenant
                if(params.enabled == null) {
                    instance.enabled = true
                }
                instance.validate()
                instance.clearErrors()
                render(view: '/lookupEntity-create', plugin: 'crm-ui-bootstrap', model: [bean: instance, beanName: beanName])
                break
            case "POST":
                instance.properties = params
                if (instance.save(flush: true)) {
                    flash.success = "${message(code: beanName + '.created.message', args: [message(code: beanName + '.label', default: beanName), instance])}"
                    redirect(action: "index")
                } else {
                    render(view: '/lookupEntity-create', plugin: 'crm-base', model: [bean: instance, beanName: beanName])
                }
                break
        }
    }

    def edit() {
        switch (request.method) {
            case "GET":
                def instance = domainClass.get(params.id)
                if (checkPermission(instance)) {
                    render(view: '/lookupEntity-edit', plugin: 'crm-ui-bootstrap', model: [bean: instance, beanName: beanName])
                } else {
                    response.sendError(404) // Not found
                }
                break
            case "POST":
                def instance = domainClass.get(params.id)
                if (checkPermission(instance)) {
                    if (params.version) {
                        def version = params.version.toLong()
                        if (instance.version > version) {
                            instance.errors.rejectValue("version", beanName + ".optimistic.locking.failure", [message(code: beanName + '.label', default: beanName)] as Object[], "Another user has updated this {0} while you were editing")
                            render(view: '/lookupEntity-edit', plugin: 'crm-ui-bootstrap', model: [bean: instance, beanName: beanName])
                            return
                        }
                    }
                    instance.properties = params
                    if (!instance.hasErrors() && instance.save(flush: true)) {
                        flash.success = "${message(code: beanName + '.updated.message', args: [message(code: beanName + '.label', default: beanName), instance])}"
                        redirect(action: "index")
                    } else {
                        render(view: '/lookupEntity-edit', plugin: 'crm-ui-bootstrap', model: [bean: instance, beanName: beanName])
                    }
                } else {
                    response.sendError(404) // Not found
                }
                break
        }
    }

    def delete() {
        def instance = domainClass.get(params.id)
        if (checkPermission(instance)) {
            def name = instance.toString()
            try {
                instance.delete(flush: true)
                flash.warning = "${message(code: beanName + '.deleted.message', args: [message(code: beanName + '.label', default: beanName), name])}"
                redirect(action: "index")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.error = "${message(code: beanName + '.not.deleted.message', args: [message(code: beanName + '.label', default: beanName), name])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            response.sendError(404) // Not found
        }
    }

    def moveUp(Long id) {
        def target = domainClass.get(id)
        if (target) {
            def sort = target.orderIndex
            def prev = domainClass.createCriteria().list([sort: 'orderIndex', order: 'desc']) {
                lt('orderIndex', sort)
                maxResults 1
            }?.find {it}
            if (prev) {
                domainClass.withTransaction {tx ->
                    target.orderIndex = prev.orderIndex
                    prev.orderIndex = sort
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
        }
        redirect action: 'index'
    }

    def moveDown(Long id) {
        def target = domainClass.get(id)
        if (target) {
            def sort = target.orderIndex
            def next = domainClass.createCriteria().list([sort: 'orderIndex', order: 'asc']) {
                gt('orderIndex', sort)
                maxResults 1
            }?.find {it}
            if (next) {
                domainClass.withTransaction {tx ->
                    target.orderIndex = next.orderIndex
                    next.orderIndex = sort
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
        }
        redirect action: 'index'
    }
}
