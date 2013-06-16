package org.pdmvalidator.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import org.dom4j.DocumentException;
import org.pdmvalidator.service.PdmService;

public class FrameAplicacao extends JFrame {

   /** A constante serialVersionUID. */
   private static final long serialVersionUID = 8878203808738316801L;
   
   private PdmService pdmService;
   private JTextArea txtArea;
   private JScrollPane scroll;
   
   public FrameAplicacao() throws DocumentException, IOException {
      setTitle("PDM Validator");
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setBounds(0,0,screenSize.width, screenSize.height);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      pdmService = new PdmService();
      txtArea = new JTextArea();
      Font font = new Font("Calibri", Font.BOLD, 11);
      txtArea.setFont(font);
      txtArea.setForeground(Color.GREEN);
      txtArea.setBackground(Color.BLACK);
      scroll = new JScrollPane(txtArea);
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      add(scroll);
      
      setLayout(new GridLayout());
      
      final JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(this);
      
      try {
         if (returnVal == JFileChooser.APPROVE_OPTION && possuiExtensaoPdm(fc)) {
            StringBuilder resultadoSb = pdmService.validar(fc.getSelectedFile());
            txtArea.setText(resultadoSb.toString());
         } else {
            JOptionPane.showMessageDialog(null,
               "Favor selecionar um arquivo com extensão \".pdm\"!", "Alerta",
               JOptionPane.ERROR_MESSAGE);
         } 
      } catch (Exception e) {
         txtArea.setText(e.getMessage());
         e.printStackTrace();
      }
   }

   private boolean possuiExtensaoPdm(final JFileChooser fc) {
      String caminhoCompleto = fc.getSelectedFile().getAbsolutePath();
      int indexExtensao = caminhoCompleto.lastIndexOf(".");
      String extensao = caminhoCompleto.substring(indexExtensao, caminhoCompleto.length());
      return extensao.equals(".pdm");
   }

}
