/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.ui.frames;

import net.miginfocom.swing.MigLayout;
import org.memoriadb.ui.moodel.ConfigurationPM;
import org.memoriadb.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ChoosDbDialog {

  private final JDialog fFrame;
  private final ConfigurationPM fModel;
  private boolean fOkButtonPressed = false;
  private JList fClassPathEntries;

  public ChoosDbDialog(ConfigurationPM configPM) {
    if (configPM == null) throw new IllegalArgumentException("Null Argument configPM");
    
    fFrame = new JDialog((Frame) null, true);
    fModel = configPM;
    createControls();
  }

  public boolean show() {
    fFrame.setVisible(true);
    return fOkButtonPressed;
  }

  private void add(JComponent component, String constaints) {
    fFrame.getContentPane().add(component, constaints);
  }

  private void configureFrame() {
    fFrame.setSize(400, 430);
    fFrame.setLocation(SwingUtil.calculateCenter(fFrame.getSize()));
    fFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    fFrame.getContentPane().setLayout(new MigLayout("fill"));
  }

  private JButton createAddFolderButton() {
    return createButton("Add folder", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dialog.showOpenDialog(fFrame);
        fModel.getClassPath().addElement(dialog.getSelectedFile().getAbsolutePath());
      }
    });
  }

  private JButton createAddJarButton() {
    return createButton("Add jar", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        FileDialog dlg = new FileDialog(fFrame, "Add jar", FileDialog.LOAD);
        dlg.setVisible(true);
        
        if (dlg.getFile() != null) {
           fModel.getClassPath().addElement(dlg.getDirectory() + dlg.getFile());
        }
        dlg.dispose(); 
      }
      
    });
  }

  private JButton createButton(String text, ActionListener listener) {
    JButton button = new JButton();
    button.setText(text);
    button.addActionListener(listener);
    return button;
  }

  private void createButtonBar() {
    add(createCancelButton(), "tag cancel, split, span");
    add(createOKButton(), "tag ok");
  }

  private JButton createCancelButton() {
    return createButton("Cancel", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fOkButtonPressed = false;
        fFrame.setVisible(false);
      }
    });
  }

  private void createChoosDbPathPart() {
    add(new JLabel("Choose a DB from File-System: "), "span");
    add(createDbPathChooserButton(), "split, span");
    JTextField dbPath = new JTextField();
    dbPath.setDocument(fModel.getDBPath());
    add(dbPath, "growx, wrap");
  }

  private void createClassPathPart() {
    fClassPathEntries = new JList();
    fClassPathEntries.setModel(fModel.getClassPath());
    add(new JScrollPane(fClassPathEntries), "h :150:, w :150:, grow");
    
    add(createAddFolderButton(), "sg classPathButton, aligny top, split, flowy");
    add(createAddJarButton(), "sg classPathButton");
    add(new JSeparator(), "growx 1");
    add(createRemoveButton(), "sg classPathButton, wrap");
  }

  private void createControls() {
    configureFrame();
    createChoosDbPathPart();
    createSeparator("ClassPath");
    createClassPathPart();
    createSeparator("");
    createButtonBar();
  }

  private JComponent createDbPathChooserButton() {
    return createButton("...", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        openFileDialog();
      }

      private void openFileDialog() {
        FileDialog dlg = new FileDialog(fFrame, "Choose DB", FileDialog.LOAD);
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
          fModel.setDbPathString(dlg.getDirectory() + dlg.getFile());
        }
        dlg.dispose();
      }
    });
  }
  
  private JButton createOKButton() {
    JButton button = createButton("OK", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        fOkButtonPressed = true;
        fFrame.setVisible(false);
      }
    });
    
    button.requestFocusInWindow();
    return button;
  }
  
  private JButton createRemoveButton() {
    return createButton("Remove", new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        Object selection = fClassPathEntries.getSelectedValue();
        if (selection == null) return;
        fModel.removeClasspathEntry(selection.toString());
      }
    });
  }

  private void createSeparator(String label) {
    add(new JLabel(label), "split, span");
    add(new JSeparator(), "growx, wrap");
  }

}
