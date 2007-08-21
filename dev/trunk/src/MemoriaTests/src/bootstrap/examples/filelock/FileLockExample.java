package bootstrap.examples.filelock;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.channels.FileLock;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class FileLockExample {

  class CloseAction extends AbstractAction {

    public CloseAction() {
      super("close");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        if(fLock != null) fLock.release();
        if (fRandomAccessFile != null) fRandomAccessFile.close();
        
        fLock=null;
        fRandomAccessFile = null;
        
        enableOpen();
      }
      catch (Exception ex) {
        enableOpen();
        ex.printStackTrace();
      }
    }
  }

  class OpenAction extends AbstractAction {


    public OpenAction() {
      super("open");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        File file = new File(fPath.getText());
        if(fWrite.isSelected()) {
          fRandomAccessFile = new RandomAccessFile(file, WRITE);
          fLock = fRandomAccessFile.getChannel().tryLock();
          if(fLock == null){
            fRandomAccessFile.close();
            throw new Exception("lock acquire failed");
          }
        }
        else {
          fRandomAccessFile = new RandomAccessFile(file, READ);
        }
        
        enableClose();
        fOpen.setBackground(null);
      }
      catch (Exception ex) {
        enableOpen();
        fOpen.setBackground(Color.red);
        ex.printStackTrace();
      }
    }
  }

  private FileLock fLock;
  private final String WRITE = "rws";
  private final String READ = "r";
  private JTextField fPath;
  private JRadioButton fRead;
  private JRadioButton fWrite;
  private final OpenAction fOpenAction;
  private final CloseAction fCloseAction;
  private RandomAccessFile fRandomAccessFile;
  private JButton fOpen;
  private JButton fClose;

  public static void main(String[] args) {
    new FileLockExample();
  }

  public FileLockExample() {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });

    fOpenAction = new OpenAction();
    fCloseAction = new CloseAction();
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the event-dispatching thread.
   */
  private void createAndShowGUI() {
    // Create and set up the window.
    JFrame frame = new JFrame("HelloWorldSwing");
    frame.setBounds(100, 100, 200, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new MigLayout("fillx", "[center][center]"));

    fPath = new JTextField();
    fPath.setText("file.txt");
    frame.getContentPane().add(fPath, "growx, span, wrap");

    fRead = new JRadioButton(READ);
    fRead.setSelected(true);
    
    fWrite = new JRadioButton(WRITE);
    ButtonGroup group = new ButtonGroup();
    group.add(fRead);
    group.add(fWrite);

    frame.getContentPane().add(fRead, "wrap, alignx left");
    frame.getContentPane().add(fWrite, "wrap, alignx left");

    fOpen = new JButton(fOpenAction);
    frame.add(fOpen, "growx");

    fClose = new JButton(fCloseAction);
    frame.add(fClose, "growx");

    // Display the window.
    // frame.pack();
    frame.setVisible(true);
  }

  private void enableClose() {
    fCloseAction.setEnabled(true);
    fOpenAction.setEnabled(false);
  }

  private void enableOpen() {
    fCloseAction.setEnabled(false);
    fOpenAction.setEnabled(true);
  }

}
