package io.jmeter.plugins.dubbo.gui


import io.jmeter.plugins.dubbo.config.DubboConfig
import org.apache.jmeter.config.gui.AbstractConfigGui
import org.apache.jmeter.testelement.TestElement

import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class DubboConfigGui extends AbstractConfigGui {
    private JTextField registry
    private JTextField facade
    private JTextField objectName
    private JTextField timeout
    private JTextField group
    private JTextField retries
    private JTextField connections
    private JComboBox<String> loadBalance
    private JComboBox<String> async
    private JComboBox<String> proxy
    private JComboBox<String> client
    private JTextField actives
    private JComboBox<String> cluster
    private JTextField filter
    private JTextField listener
    private JComboBox<String> init
    private JButton help
    private final String[] loadBalanceOptions = ['random', 'roundrobin', 'leastactive']
    private final String[] asyncOptions = ['true', 'false']
    private final String[] proxyOptions = ['javassist', 'jdk']
    private final String[] clientOptions = ['netty', 'mina']
    private final String[] clusterOptions = ['failover', 'failfast', 'failsafe', 'failback', 'forking']
    private final String[] initOptions = ['true', 'false']

    private final String TEXT = 'Dubbo Configuration'

    DubboConfigGui() {
        super()
        init()
        initGuiValues()
    }

    @Override
    String getLabelResource() {
        return 'dubbo_configuration'
    }

    String getStaticLabel() {
        return TEXT
    }

    @Override
    TestElement createTestElement() {
        DubboConfig config = new DubboConfig()
        config.setName(TEXT)
        modifyTestElement(config)
        return config
    }

    @Override
    void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement)
        DubboConfig config = (DubboConfig) testElement
        config.setName(this.name)
        config.setComment(this.comment)
        config.setDubboObject(this.objectName.getText())
        config.setRegistry(this.registry.getText())
        config.setFacade(this.facade.getText())
        config.setTimeout(this.timeout.getText() as Integer)
        config.setGroup(this.group.getText())
        config.setRetries(this.retries.getText())
        config.setConnections(this.connections.getText())
        config.setLoadBalance(this.loadBalance.getSelectedItem().toString())
        config.setAsync(this.async.getSelectedItem().toString())
        config.setProxy(this.proxy.getSelectedItem().toString())
        config.setClient(this.client.getSelectedItem().toString())
        config.setActives(this.actives.toString())
        config.setCluster(this.cluster.getSelectedItem().toString())
        config.setFilter(this.filter.getText())
        config.setListener(this.listener.getText())
        config.setInit(this.init.getSelectedItem().toString())
    }

    void configure(TestElement element) {
        super.configure(element)
        DubboConfig config = (DubboConfig) element
        registry.setText(config.registry)
        facade.setText(config.facade)
        timeout.setText(config.timeout.toString())
        objectName.setText(config.dubboObject)
        group.setText(config.group)
        retries.setText(config.retries.toString())
        connections.setText(config.connections.toString())
        loadBalance.setSelectedItem(config.loadBalance)
        async.setSelectedItem(config.async.toString())
        proxy.setSelectedItem(config.proxy)
        client.setSelectedItem(config.client)
        actives.setText(config.actives.toString())
        cluster.setSelectedItem(config.cluster)
        filter.setText(config.filter)
        listener.setText(config.listener)
        init.setSelectedItem(config.init.toString())
    }

    private void init() {
        setLayout(new BorderLayout())
        setBorder(makeBorder())
        Box box = Box.createVerticalBox()
        box.add(makeTitlePanel())
        help = new JButton()
        Image img = ImageIO.read(getClass().getResource("/help.png"))
        help.setIcon(new ImageIcon(img))
        help.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                Desktop.getDesktop().browse(new URL('https://cn.dubbo.apache.org/zh-cn/overview/home/').toURI())
            }
        })
        box.add(help)
        add(box, BorderLayout.NORTH)
        JPanel mainPanel = new JPanel(new GridBagLayout())

        GridBagConstraints labelConstraints = new GridBagConstraints()
        GridBagConstraints editConstraints = new GridBagConstraints()
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START
        editConstraints.weightx = 1.0
        editConstraints.fill = GridBagConstraints.HORIZONTAL

        addToPanel(mainPanel, labelConstraints, 0, 0, 1, new JLabel('Registry: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 0, 1, 5, registry = new JTextField())
        addToPanel(mainPanel, labelConstraints, 1, 0, 1, new JLabel('Interface: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 1, 1, 5, facade = new JTextField())
        addToPanel(mainPanel, labelConstraints, 2, 0, 1, new JLabel('Dubbo variable: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 2, 1, 1, objectName = new JTextField())
        addToPanel(mainPanel, labelConstraints, 2, 2, 1, new JLabel('Timeout: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 2, 3, 1, timeout = new JTextField('1000'))
        addToPanel(mainPanel, labelConstraints, 2, 4, 1, new JLabel('Group: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 2, 5, 1, group = new JTextField(''))
        addToPanel(mainPanel, labelConstraints, 3, 0, 1, new JLabel('Retries: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 3, 1, 1, retries = new JTextField('2'))
        addToPanel(mainPanel, labelConstraints, 3, 2, 1, new JLabel('Connections: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 3, 3, 1, connections = new JTextField('100'))
        addToPanel(mainPanel, labelConstraints, 3, 4, 1, new JLabel('LoadBalance: ', JLabel.LEFT))
        loadBalance = new JComboBox<String>(loadBalanceOptions)
        loadBalance.setEditable(true)
        async = new JComboBox<String>(asyncOptions)
        async.setEditable(true)
        proxy = new JComboBox<String>(proxyOptions)
        proxy.setEditable(true)
        client = new JComboBox<String>(clientOptions)
        client.setEditable(true)
        cluster = new JComboBox<String>(clusterOptions)
        cluster.setEditable(true)
        init = new JComboBox<String>(initOptions)
        init.setEditable(true)
        addToPanel(mainPanel, editConstraints, 3, 5, 1, loadBalance)
        addToPanel(mainPanel, labelConstraints, 4, 0, 1, new JLabel('Async: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 4, 1, 1, async)
        addToPanel(mainPanel, labelConstraints, 4, 2, 1, new JLabel('Proxy: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 4, 3, 1, proxy)
        addToPanel(mainPanel, labelConstraints, 4, 4, 1, new JLabel('Client: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 4, 5, 1, client)
        addToPanel(mainPanel, labelConstraints, 5, 0, 1, new JLabel('Actives: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 5, 1, 1, actives = new JTextField('0'))
        addToPanel(mainPanel, labelConstraints, 5, 2, 1, new JLabel('Cluster: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 5, 3, 1, cluster)
        addToPanel(mainPanel, labelConstraints, 5, 4, 1, new JLabel('Filter: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 5, 5, 1, filter = new JTextField('default'))
        addToPanel(mainPanel, labelConstraints, 6, 0, 1, new JLabel('Listener: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 6, 1, 1, listener = new JTextField('default'))
        addToPanel(mainPanel, labelConstraints, 6, 2, 1, new JLabel('Init: ', JLabel.LEFT))
        addToPanel(mainPanel, editConstraints, 6, 3, 1, init)

        Box mainBox = Box.createVerticalBox()
        mainBox.setBorder(BorderFactory.createTitledBorder('Configuration'))
        mainBox.add(mainPanel)

        JPanel container = new JPanel(new BorderLayout())
        container.add(mainBox, BorderLayout.NORTH)
        add(container, BorderLayout.CENTER)
    }

    private
    static void addToPanel(JPanel panel, GridBagConstraints constraints, int row, int col, int width, JComponent component) {
        constraints.gridx = col
        constraints.gridy = row
        constraints.gridwidth = width
        panel.add(component, constraints)
    }

    private void initGuiValues() {
        registry.setText('')
        facade.setText('')
        objectName.setText('')
        timeout.setText('1000')
        group.setText('')
        retries.setText('2')
        connections.setText('100')
        loadBalance.setSelectedIndex(0)
        async.setSelectedItem('false')
        proxy.setSelectedIndex(0)
        client.setSelectedIndex(0)
        actives.setText('0')
        cluster.setSelectedIndex(0)
        filter.setText('default')
        listener.setText('default')
        init.setSelectedItem('false')
    }

    void clearGui() {
        super.clearGui()
        initGuiValues()
    }
}
