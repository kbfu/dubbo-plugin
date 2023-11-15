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

package io.jmeter.plugins.dubbo.config

import org.apache.dubbo.config.ApplicationConfig
import org.apache.dubbo.config.ReferenceConfig
import org.apache.dubbo.config.RegistryConfig
import org.apache.dubbo.rpc.service.GenericService
import org.apache.jmeter.config.ConfigTestElement
import org.apache.jmeter.engine.util.NoThreadClone
import org.apache.jmeter.testelement.TestStateListener
import org.apache.jmeter.threads.JMeterVariables
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class DubboConfig extends ConfigTestElement implements TestStateListener, NoThreadClone {
    private transient ReferenceConfig<GenericService> referenceConfig
    private static final Logger log = LoggerFactory.getLogger(DubboConfig.class)
    static final NAME = 'dubboConfig'
    private final String TIMEOUT = 'timeout'
    private final String REGISTRY = 'registry'
    private final String FACADE = 'facade'
    private final String DUBBO_OBJECT = 'dubboObject'
    private final String GROUP = 'group'
    private final String RETRIES = 'retries'
    private final String CONNECTIONS = 'connections'
    private final String LOAD_BALANCE = 'loadBalance'
    private final String ASYNC = 'async'
    private final String PROXY = 'proxy'
    private final String CLIENT = 'client'
    private final String ACTIVES = 'actives'
    private final String CLUSTER = 'cluster'
    private final String FILTER = 'filter'
    private final String LISTENER = 'listener'
    private final String INIT = 'init'

    void setTimeout(Integer timeout) {
        setProperty(TIMEOUT, timeout)
    }

    void setRegistry(String registry) {
        setProperty(REGISTRY, registry)
    }

    void setFacade(String facade) {
        setProperty(FACADE, facade)
    }

    void setDubboObject(String dubboObject) {
        setProperty(DUBBO_OBJECT, dubboObject)
    }

    void setGroup(String group) {
        setProperty(GROUP, group)
    }

    void setRetries(String retries) {
        setProperty(RETRIES, retries)
    }

    void setConnections(String connections) {
        setProperty(CONNECTIONS, connections)
    }

    void setLoadBalance(String loadBalance) {
        setProperty(LOAD_BALANCE, loadBalance)
    }

    void setAsync(String async) {
        setProperty(ASYNC, async)
    }

    void setProxy(String proxy) {
        setProperty(PROXY, proxy)
    }

    void setClient(String client) {
        setProperty(CLIENT, client)
    }

    void setActives(String actives) {
        setProperty(ACTIVES, actives)
    }

    void setCluster(String cluster) {
        setProperty(CLUSTER, cluster)
    }

    void setFilter(String filter) {
        setProperty(FILTER, filter)
    }

    void setListener(String listener) {
        setProperty(LISTENER, listener)
    }

    void setInit(String init) {
        setProperty(INIT, init)
    }

    int getTimeout() {
        return getPropertyAsInt(TIMEOUT)
    }

    String getRegistry() {
        return getPropertyAsString(REGISTRY)
    }

    String getFacade() {
        return getPropertyAsString(FACADE)
    }

    String getDubboObject() {
        return getPropertyAsString(DUBBO_OBJECT)
    }

    String getGroup() {
        return getPropertyAsString(GROUP)
    }

    int getRetries() {
        return getPropertyAsInt(RETRIES)
    }

    int getConnections() {
        return getPropertyAsInt(CONNECTIONS)
    }

    String getLoadBalance() {
        return getPropertyAsString(LOAD_BALANCE)
    }

    boolean getAsync() {
        return getPropertyAsBoolean(ASYNC)
    }

    String getProxy() {
        return getPropertyAsString(PROXY)
    }

    String getClient() {
        return getPropertyAsString(CLIENT)
    }

    int getActives() {
        return getPropertyAsInt(ACTIVES)
    }

    String getCluster() {
        return getPropertyAsString(CLUSTER)
    }

    String getFilter() {
        return getPropertyAsString(FILTER)
    }

    String getListener() {
        return getPropertyAsString(LISTENER)
    }

    boolean getInit() {
        return getPropertyAsBoolean(INIT)
    }

    @Override
    void testStarted() {
        try {
            try {
                synchronized (this) {
                    JMeterVariables variables = getThreadContext().variables
                    if (variables.getObject(dubboObject) == null) {
                        String registry = this.registry
                        int timeout = this.timeout
                        String facade = this.facade
                        String group = this.group
                        int retries = this.retries
                        int connections = this.connections
                        String loadBalance = this.loadBalance
                        boolean async = this.async
                        String proxy = this.proxy
                        String client = this.client
                        int actives = this.actives
                        String cluster = this.cluster
                        String filter = this.filter
                        String listener = this.listener
                        boolean init = this.init
                        log.info("================Dubbo Configuration Info================")
                        log.info("Registry: $registry")
                        log.info("Timeout: $timeout")
                        log.info("Facade: $facade")
                        log.info("Group: $group")
                        log.info("Retries: $retries")
                        log.info("Connections: $connections")
                        log.info("LoadBalance: $loadBalance")
                        log.info("Async: $async")
                        log.info("Proxy: $proxy")
                        log.info("Client: $client")
                        log.info("Actives: $actives")
                        log.info("Cluster: $cluster")
                        log.info("Filter: $filter")
                        log.info("Listener: $listener")
                        log.info("Init: $init")

                        referenceConfig = new ReferenceConfig<>()
                        ApplicationConfig applicationConfig = new ApplicationConfig()
                        applicationConfig.setName(NAME)
                        RegistryConfig registryConfig = new RegistryConfig()
                        registryConfig.setAddress(registry)
                        applicationConfig.setRegistry(registryConfig)
                        referenceConfig.setApplication(applicationConfig)
                        referenceConfig.setTimeout(timeout)
                        referenceConfig.setInterface(facade)
                        referenceConfig.setGeneric("true")
                        referenceConfig.setGroup(group)
                        referenceConfig.setRetries(retries)
                        referenceConfig.setConnections(connections)
                        referenceConfig.setLoadbalance(loadBalance)
                        referenceConfig.setAsync(async)
                        referenceConfig.setProxy(proxy)
                        referenceConfig.setClient(client)
                        referenceConfig.setActives(actives)
                        referenceConfig.setCluster(cluster)
                        referenceConfig.setFilter(filter)
                        referenceConfig.setListener(listener)
                        referenceConfig.setInit(init)
                        GenericService o = referenceConfig.get()
                        log.info("generic service $o")
                        variables.putObject(dubboObject, o)
                    }
                }
            } catch (Exception e) {
                log.error(e.toString())
            }
        } catch (IllegalStateException e) {
            log.error(e.toString())
        }
    }

    @Override
    void testStarted(String s) {
        testStarted()
    }

    @Override
    void testEnded() {
        synchronized (this) {
            if (referenceConfig != null) {
                referenceConfig.destroy()
            }
        }
    }

    @Override
    void testEnded(String s) {
        testEnded()
    }
}
