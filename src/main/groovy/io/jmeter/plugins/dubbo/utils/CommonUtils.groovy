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
