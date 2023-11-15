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

package io.jmeter.plugins.dubbo.utils


import io.jmeter.plugins.dubbo.sampler.DubboDataTable

import javax.swing.table.DefaultTableModel

class CommonUtils {
    static Vector<Vector<String>> toVectors(List<DubboDataTable> list) {
        Vector<Vector<String>> res = new Vector<Vector<String>>()
        for (DubboDataTable args : list) {
            Vector<String> v = new Vector<String>()
            v.add(args.value)
            v.add(args.type)
            res.add(v)
        }
        return res
    }

    static List<DubboDataTable> toListTable(DefaultTableModel model) {
        List<DubboDataTable> dataTable = new ArrayList<DubboDataTable>()
        for (int i = 0; i < model.rowCount; i++) {
            DubboDataTable row = new DubboDataTable()
            String value = (model.getValueAt(i, 0) as String).replace('"', '\\"')
            String type = model.getValueAt(i, 1)
            row.setValue(value)
            row.setType(type)
            dataTable.push(row)
        }
        return dataTable
    }
}
