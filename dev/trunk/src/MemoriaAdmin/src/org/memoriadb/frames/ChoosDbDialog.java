package org.memoriadb.frames;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import org.memoriadb.model.Configuration;
import org.memoriadb.util.SwingUtil;

public final class ChoosDbDialog {

  private final JDialog fFrame;
  private String fDbPath = "";
  private JTextField fDbPathTextField;

  public ChoosDbDialog() {
    fFrame = new JDialog((Frame) null, true);
    fFrame.setSize(400, 130);
    fFrame.setLocation(SwingUtil.calculateCenter(fFrame.getSize()));
    fFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    createControls();
  }

  public Configuration show() {
    fFrame.setVisible(true);
    Configuration configuration = new Configuration();
    configuration.setDbPath(fDbPath);
    return configuration;
  }

  private void add(JComponent component, String constaints) {
    fFrame.getContentPane().add(component, constaints);
  }

  private JButton createButton(String text, ActionListener listener) {
    JButton button = new JButton();
    button.setText(text);
    button.addActionListener(listener);
    return button;
  }

  private JButton createCancelButton() {
    return createButton("Cancel", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fFrame.setVisible(false);
      }
    });
  }

  private void createControls() {
    fFrame.getContentPane().setLayout(new MigLayout("fillx, nogrid"));
    add(new JLabel("Choose a DB from File-System: "), "wrap");
    add(createDbPathChooserButton(), "");
    fDbPathTextField = new JTextField();
    add(fDbPathTextField, "growx, wrap");
    add(new JSeparator(), "growx, wrap");
    add(createCancelButton(), "tag cancel");
    add(createOKButton(), "tag ok");
  }
  
  private JComponent createDbPathChooserButton() {
    return createButton("...", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        openFileDialog();
      }

      private void openFileDialog() {
        FileDialog dlg = new FileDialog(fFrame, "Choose DB", FileDialog.LOAD);
        dlg.setLocation(100, 0);
        dlg.setLocationRelativeTo(fFrame);
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
          fDbPathTextField.setText( dlg.getDirectory() + dlg.getFile()); 
        }
        dlg.dispose();
      }
    });
  }

  private JButton createOKButton() {
    return createButton("OK", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fFrame.setVisible(false);
        fDbPath = fDbPathTextField.getText(); 
      }
    });
  }

}
