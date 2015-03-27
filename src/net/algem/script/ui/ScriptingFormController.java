package net.algem.script.ui;

import net.algem.script.common.Script;
import net.algem.script.directory.ScriptDirectoryService;
import net.algem.script.directory.models.ScriptDirectory;
import net.algem.script.directory.models.ScriptImplFile;
import net.algem.util.module.GemDesktop;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScriptingFormController {
    private JPanel panel1;
    private JTree tree1;
    private JButton runButton;
    private JTable table1;

    private final GemDesktop desktop;
    private ScriptDirectoryService scriptDirectoryService;
    private Script script;

    public ScriptingFormController(GemDesktop desktop) {
        this.desktop = desktop;
        scriptDirectoryService = desktop.getDataCache().getScriptDirectoryService();

        $$$setupUI$$$();
        loadScripts();

        tree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    TreePath selPath = tree1.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null) {
                        Object selectedComponent = selPath.getLastPathComponent();
                        if (selectedComponent instanceof ScriptImplFile) {
                          openScript((ScriptImplFile)selectedComponent) ;
                        }
                    }
                }
            }
        });
    }

    private void openScript(ScriptImplFile scriptFile) {
        try {
            script = scriptDirectoryService.loadScript(scriptFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JPanel getPanel() {
        return panel1;
    }

    private void loadScripts() {

        ScriptDirectory availableScripts = scriptDirectoryService.getAvailableScripts();
        tree1.setModel(new ScriptDirectoryTreeModel(availableScripts));
    }

    private void createUIComponents() {
        tree1 = new JTree() {
            @Override
            public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof ScriptDirectory) {
                    return ((ScriptDirectory) value).getDirectory().getName();
                } else if (value instanceof ScriptImplFile) {
                    return ((ScriptImplFile) value).getCodeFile().getName();
                }
            }
        };
        // TODO: place custom component creation code here
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(true);
        panel1.setPreferredSize(new Dimension(1024, 768));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(250);
        panel1.add(splitPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel2);
        tree1.setMaximumSize(new Dimension(250, 100));
        tree1.setPreferredSize(new Dimension(250, 100));
        tree1.setRequestFocusEnabled(true);
        panel2.add(tree1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        panel2.add(toolBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel3);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        runButton = new JButton();
        runButton.setText("Executer");
        panel3.add(runButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        table1 = new JTable();
        panel3.add(table1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
