package org.pdmvalidator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.pdmvalidator.swing.FrameAplicacao;

public class Aplicacao {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               FrameAplicacao frame = new FrameAplicacao();
               frame.setVisible(true);
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

}
