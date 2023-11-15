/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmeter.plugins.dubbo.sampler

import io.jmeter.plugins.dubbo.utils.Json
import org.apache.dubbo.rpc.service.GenericService
import org.apache.jmeter.samplers.AbstractSampler
import org.apache.jmeter.samplers.Entry
import org.apache.jmeter.samplers.SampleResult
import org.apache.jmeter.threads.JMeterVariables
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DubboSampler extends AbstractSampler {
    private static final Logger log = LoggerFactory.getLogger(DubboSampler.class)
    private final String DUBBO_OBJECT = 'dubboObject'
    private final String TABLE = 'table'
    private final String METHOD = 'method'

    void setDubboObject(String dubboObject) {
        setProperty(DUBBO_OBJECT, dubboObject)
    }

    void setTable(List<DubboDataTable> table) {
        String tableString = Json.convertToString(table)
        setProperty(TABLE, tableString)
    }

    void setMethod(String method) {
        setProperty(METHOD, method)
    }

    String getDubboObject() {
        return getPropertyAsString(DUBBO_OBJECT)
    }

    List<DubboDataTable> getTable() {
        String tableString = getProperty(TABLE)
        List<DubboDataTable> table = Json.convertToObject(tableString, List) as List<DubboDataTable>
        return table
    }

    String getMethod() {
        return getPropertyAsString(METHOD)
    }

    @Override
    SampleResult sample(Entry entry) {
        SampleResult sr = new SampleResult()
        sr.setSampleLabel(getName())
        sr.setDataType(SampleResult.TEXT)
        sr.sampleStart()
        try {
            sr.setSamplerData(getPropertyAsString(TABLE))
            sr.setResponseData(invoke(), null)
            sr.setResponseOK()
        } catch (Exception e) {
            sr.setResponseData(e.toString(), null)
            sr.setSuccessful(false)
        } finally {
            sr.sampleEnd()
        }
        return sr
    }

    private String invoke() {
        JMeterVariables variables = getThreadContext().variables
        String objectName = getDubboObject()
        List<DubboDataTable> list = getTable()
        String method = getMethod()
        List<String> types = new ArrayList<>()
        List<String> values = new ArrayList<>()

        list.each { row ->
            types.push(row.type)
            values.push(row.value)
        }

        GenericService ref = variables.getObject(objectName)
        log.info("ref: $ref")
        list.each { row -> log.info("$row.value $row.type") }
        return Json.convertToString(ref.$invoke(method, types.toArray() as String[], values.toArray()))
    }
}
