package io.jmeter.plugins.dubbo.gui


import io.jmeter.plugins.dubbo.sampler.DubboSampler
import io.jmeter.plugins.dubbo.utils.CommonUtils
import org.apache.jmeter.samplers.gui.AbstractSamplerGui
import org.apache.jmeter.testelement.TestElement

import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class DubboSamplerGui extends AbstractSamplerGui {
    private JTextField objectName
    private DefaultTableModel model
    private JTable table
    private JTextField method
    private final Vector<String> COLUMNS = ['value', 'type']
    private final String[] COLUMN_NAMES = ['value', 'type']
    private final String[] NEW_ROW = ['', '']
    private final String TEXT = 'Dubbo Sampler'

    DubboSamplerGui() {
        super()
        init()
    }

    @Override
    String getLabelResource() {
        return 'dubbo_sampler'
    }

    String getStaticLabel() {
        return TEXT
    }

    @Override
    TestElement createTestElement() {
        DubboSampler sampler = new DubboSampler()
        sampler.setName(TEXT)
        modifyTestElement(sampler)
        return sampler
    }

    @Override
    void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement)
        DubboSampler sampler = (DubboSampler) testElement
        sampler.setName(this.name)
        sampler.setComment(this.comment)
        sampler.setDubboObject(this.objectName.getText())

        // save table
        try {
            table.getCellEditor().stopCellEditing()
            model.fireTableDataChanged()
        } catch (NullPointerException ignored) {

        }
        sampler.setTable(CommonUtils.toListTable(this.model))
        sampler.setMethod(this.method.getText())
    }

    void configure(TestElement element) {
        super.configure(element)
        DubboSampler sampler = (DubboSampler) element
        objectName.setText(sampler.getDubboObject())
        model.setDataVector(CommonUtils.toVectors(sampler.getTable()), COLUMNS)
        method.setText(sampler.getMethod())
    }

    private void init() {
        setLayout(new BorderLayout())
        setBorder(makeBorder())
        Box box = Box.createVerticalBox()
        box.add(makeTitlePanel())
        add(box, BorderLayout.NORTH)

        // config section
        GridBagConstraints labelConstraints = new GridBagConstraints()
        GridBagConstraints editConstraints = new GridBagConstraints()
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START
        editConstraints.weightx = 1.0
        editConstraints.fill = GridBagConstraints.HORIZONTAL
        JPanel mainPanel = new JPanel(new GridBagLayout())
        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel('Dubbo variable: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 0, 1, objectName = new JTextField())
        addToPanel(mainPanel, labelConstraints, 1, 0, new JLabel('Method: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 1, 1, method = new JTextField())
        Box configBox = Box.createVerticalBox()
        configBox.setBorder(BorderFactory.createTitledBorder('Configuration'))
        configBox.add(mainPanel)

        // table section
        model = new DefaultTableModel(COLUMNS, 0)
        table = new JTable(model)
        table.getTableHeader().setReorderingAllowed(false)
        table.setRowHeight(20)
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION)
        JScrollPane scrollPane = new JScrollPane(table)
        JPanel tablePanel = new JPanel(new GridBagLayout())
        GridBagConstraints tableConstrains = new GridBagConstraints()
        tableConstrains.anchor = GridBagConstraints.FIRST_LINE_START
        tableConstrains.weightx = 1.0
        tableConstrains.fill = GridBagConstraints.HORIZONTAL
        addToPanel(tablePanel, tableConstrains, 0, 0, configBox)
        addToPanel(tablePanel, tableConstrains, 1, 0, scrollPane)
        add(tablePanel, BorderLayout.CENTER)

        // button section
        JButton addBtn = new JButton('Add')
        addBtn.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (table.rowCount != 0) {
                    table.removeRowSelectionInterval(0, table.rowCount - 1)
                }
                model.addRow(NEW_ROW)
                table.addRowSelectionInterval(table.rowCount - 1, table.rowCount - 1)
            }
        })
        JButton delBtn = new JButton('Delete')
        delBtn.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (model.rowCount > 0) {
                    int lastRow
                    table.removeEditor()
                    lastRow = table.getSelectedRows()[-1]
                    int i = 0
                    for (int row : table.getSelectedRows()) {
                        model.removeRow(row - i)
                        i++
                    }
                    if (lastRow <= table.rowCount - 1) {
                        table.getSelectedRows().each { row -> table.removeRowSelectionInterval(row, row) }
                        table.addRowSelectionInterval(lastRow, lastRow)
                    } else if (table.rowCount >= 1) {
                        table.addRowSelectionInterval(table.rowCount - 1, table.rowCount - 1)
                    }
                }
            }
        })
        JButton upBtn = new JButton('Up')
        upBtn.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (table.selectedRows.length > 0) {
                    try {
                        table.getCellEditor().stopCellEditing()
                    } catch (NullPointerException ignored) {

                    }
                    int[] selected = table.selectedRows
                    int start = selected[0]
                    int end = selected[-1]
                    if (start > 0) {
                        model.moveRow(start, end, start - 1)
                        table.removeRowSelectionInterval(start, end)
                        table.addRowSelectionInterval(start - 1, end - 1)
                    }
                }
            }
        })
        JButton downBtn = new JButton('Down')
        downBtn.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (table.selectedRows.length > 0) {
                    try {
                        table.getCellEditor().stopCellEditing()
                    } catch (NullPointerException ignored) {

                    }
                    int[] selected = table.selectedRows
                    int start = selected[0]
                    int end = selected[-1]
                    if (end + 1 < model.rowCount) {
                        model.moveRow(start, end, start + 1)
                        table.removeRowSelectionInterval(start, end)
                        table.addRowSelectionInterval(start + 1, end + 1)
                    }
                }
            }
        })

        JPanel btnPanel = new JPanel(new GridBagLayout())
        GridBagConstraints btnConstrains = new GridBagConstraints()
        tableConstrains.anchor = GridBagConstraints.FIRST_LINE_START
        tableConstrains.weightx = 1.0
        tableConstrains.fill = GridBagConstraints.HORIZONTAL
        addToPanel(btnPanel, btnConstrains, 0, 0, addBtn)
        addToPanel(btnPanel, btnConstrains, 0, 1, delBtn)
        addToPanel(btnPanel, btnConstrains, 0, 2, upBtn)
        addToPanel(btnPanel, btnConstrains, 0, 3, downBtn)

        add(btnPanel, BorderLayout.SOUTH)
    }

    private
    static void addToPanel(JPanel panel, GridBagConstraints constraints, int row, int col, JComponent component) {
        constraints.gridx = col
        constraints.gridy = row
        panel.add(component, constraints)
    }

    private void initGuiValues() {
        objectName.setText('')
        method.setText('')
        model.setDataVector(null, COLUMN_NAMES)
    }

    void clearGui() {
        super.clearGui()
        initGuiValues()
    }

}
