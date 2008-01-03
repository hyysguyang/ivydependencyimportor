package org.ivydependencyimportor.ivy.idea.preferences;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.Spacer;
import gz.plugin.ivy.Setting;
import gz.plugin.ivy.Log;

/**
 * @author <a href="mailto:gzkaneg@gmail.com">gzkaneg</a>
 */
public class PreferencesPanel
{
    private JPanel mainPanel;
    private TextFieldWithBrowseButton distLibPath;
    private Setting setting = Setting.getSetting();
    private JButton help;
    private JTextField artifactPattern;
    private JTextField fullUrl;

    public PreferencesPanel()
    {
        distLibPath.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Application application = ApplicationManager.getApplication();
                application.runWriteAction(new Runnable()
                {
                    public void run()
                    {
                        VirtualFile previous = LocalFileSystem.getInstance().refreshAndFindFileByPath(
                            distLibPath.getText().replace('\\', '/')
                        );

                        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory
                            .createSingleFolderDescriptor();
                        fileDescriptor.setShowFileSystemRoots(true);
                        fileDescriptor.setTitle("Select lib path");
                        fileDescriptor.setDescription("Select lib path for dist");
                        VirtualFile[] virtualFiles = FileChooser.chooseFiles(distLibPath, fileDescriptor, previous);

                        Log.log(virtualFiles.length);
                        if (virtualFiles.length > 0) {
                            String path = virtualFiles[0].getPath();
                            Log.log(path);
                            if (!path.endsWith("/")) {
                                path = path + "/";
                            }
                            distLibPath.setText(path);
                            fullUrl.setText(distLibPath.getText() + artifactPattern.getText());
                        }
                    }
                });
            }
        });

        help.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String doc = "/help/help.html";
                perform(doc);
            }
        });
        artifactPattern.addKeyListener(new KeyListener()
        {
            public void keyTyped(KeyEvent e)
            {

            }

            public void keyPressed(KeyEvent e)
            {

            }

            public void keyReleased(KeyEvent e)
            {
                fullUrl.setText(distLibPath.getText() + artifactPattern.getText());
            }
        });
    }

    public void perform(String fDocument)
    {
        final JFrame helpDialog = new JFrame();
        helpDialog.setDefaultCloseOperation(2);
        final JEditorPane html = new JEditorPane();
        html.setAutoscrolls(true);
        html.setEditable(false);
        html.setEditorKit(new HTMLEditorKit());

        JScrollPane scroller = new JScrollPane(html);
        helpDialog.getContentPane().add(scroller, "Center");
        try {
            html.setPage(getClass().getResource(fDocument));
        }
        catch (IOException e1) {
            html.setText(e1.toString());
        }
        center(helpDialog, 500, 500);
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                helpDialog.setVisible(true);
                helpDialog.toFront();
                helpDialog.setFocusable(true);
            }
        });
    }

    private void center(Component c, int width, int height)
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        c.setBounds((screen.width - width) / 2, (screen.height - height) / 2, width, height);
    }

    JPanel getPanel()
    {
        return mainPanel;
    }

    public boolean isChanged()
    {
        fullUrl.setText(distLibPath.getText() + artifactPattern.getText());
        Log.log(distLibPath.getText());
        Log.log(artifactPattern.getText());
        Log.log(setting.getArtifactPattern());
        return !distLibPath.getText().equals(setting.getDistPath()) ||
               !artifactPattern.getText().equals(setting.getArtifactPattern());
    }

    public void saveSettings()
    {
        setting.setDistPath(distLibPath.getText());
        setting.setArtifactPattern(artifactPattern.getText());
    }

    public void readSettings()
    {
        distLibPath.setText(setting.getDistPath());
        artifactPattern.setText(setting.getArtifactPattern());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle
            .getBundle("gz/plugin/ivy/res/ivy").getString("configuration")));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1,
                                 ResourceBundle.getBundle("gz/plugin/ivy/res/ivy").getString("distribution_libs_path"));
        mainPanel.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                                                  GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                                                  null, null, null, 0, false));
        distLibPath = new TextFieldWithBrowseButton();
        distLibPath.setEditable(true);
        mainPanel.add(distLibPath, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
                                                       GridConstraints.FILL_HORIZONTAL, GridConstraints
            .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK |
                                                                           GridConstraints.SIZEPOLICY_CAN_GROW, null,
                                                                                                                null,
                                                                                                                null, 0,
                                                                                                                false));
        artifactPattern = new JTextField();
        artifactPattern.setText("");
        mainPanel.add(artifactPattern, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST,
                                                           GridConstraints.FILL_HORIZONTAL,
                                                           GridConstraints.SIZEPOLICY_WANT_GROW,
                                                           GridConstraints.SIZEPOLICY_FIXED, null,
                                                           new Dimension(150, -1), null, 0, false));
        fullUrl = new JTextField();
        fullUrl.setEditable(false);
        fullUrl.setEnabled(true);
        mainPanel.add(fullUrl, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST,
                                                   GridConstraints.FILL_HORIZONTAL,
                                                   GridConstraints.SIZEPOLICY_WANT_GROW,
                                                   GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null,
                                                   0, false));
        help = new JButton();
        help.setText("Button");
        help.setVisible(false);
        mainPanel.add(help, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
                                                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK |
                                                                                 GridConstraints.SIZEPOLICY_CAN_GROW,
                                                GridConstraints.SIZEPOLICY_FIXED, null,
                                                null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                                                   GridConstraints.FILL_VERTICAL, 1,
                                                   GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null,
                                                   new Dimension(-1, 20), 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                                                   GridConstraints.FILL_VERTICAL, 1,
                                                   GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null,
                                                   new Dimension(-1, 20), 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
                                                   GridConstraints.FILL_VERTICAL, 1,
                                                   GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null,
                                                   new Dimension(-1, 20), 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2,
                                 ResourceBundle.getBundle("gz/plugin/ivy/res/ivy").getString("artifact_pattern"));
        mainPanel.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                                                  GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                                                  null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3,
                                 ResourceBundle.getBundle("gz/plugin/ivy/res/ivy").getString("full_artifact_url"));
        mainPanel.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                                                  GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                                                  null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text)
    {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) {
                    break;
                }
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}