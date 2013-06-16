package org.pdmvalidator.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.pdmvalidator.model.ColunaPdm;
import org.pdmvalidator.model.TabelaPdm;

/**
 * PdmService.
 * @author nicolas.ferreira
 */
public class PdmService
{

   public StringBuilder validar(File arquivoPdm) throws DocumentException, IOException {
      StringBuilder comentarioSb = new StringBuilder();
      StringBuilder historicoSb = new StringBuilder();
      Map<String, TabelaPdm> mapaTabelas = montarMapaTabelas(arquivoPdm.getAbsolutePath());
      
      int countProblemaComentario = 0;
      for (TabelaPdm tabela : mapaTabelas.values()) {
         countProblemaComentario = validarComentarioTabela(tabela, comentarioSb, countProblemaComentario);
      }
      
      for (TabelaPdm tabela : mapaTabelas.values()) {
         if (tabela.getNome().startsWith("TH_")) {
            deparaTabelaHistorico(mapaTabelas, tabela, historicoSb);
         }
      }
      
      return new StringBuilder(comentarioSb.toString() + "\n\n" + historicoSb.toString());
   }

   private int validarComentarioTabela(TabelaPdm tabela, StringBuilder comentarioSb, int countProblemaComentario) {
      String strProblemaComentario = "* Falta comentário em: \n";
      if (tabela.naoPossuiComentario()) {
         countProblemaComentario++;
         verificaNecessidadePreencherBuilder(countProblemaComentario, comentarioSb, strProblemaComentario);
         comentarioSb.append(" Tabela " + tabela.getNome() + " \n");
      }
      
      for (ColunaPdm coluna : tabela.getMapaColuna().values()) {
         if (coluna.naoPossuiComentario()) {
            countProblemaComentario++;
            verificaNecessidadePreencherBuilder(countProblemaComentario, comentarioSb, strProblemaComentario);
            comentarioSb.append(" Couna " + coluna.getNome() + " \n");
         }
      }
      return countProblemaComentario;
   }

   private void deparaTabelaHistorico(Map<String, TabelaPdm> mapaTabelas, TabelaPdm tabelaHistorico, StringBuilder historicoSb) {
      String nometb = tabelaHistorico.getNome().replace("TH_", "TB_");
      TabelaPdm tabela = mapaTabelas.get(nometb);
      
      if (tabela != null) {
         deparaColunasHistorico(tabela, tabelaHistorico, historicoSb);
      } else {
         historicoSb.append("* Histórico sem Tabela: " + nometb + "\n");
      }
   }

   private void deparaColunasHistorico(TabelaPdm tabela, TabelaPdm tabelaHistorico, StringBuilder historicoSb) {
      String strProblemaHistorico = "\n* Colunas com problemas no histórico: " + tabelaHistorico.getNome() + "\n";
      int countProblemaHistorico = 0;
      for (ColunaPdm coluna : tabela.getMapaColuna().values()) {
         ColunaPdm colunaHistorico = tabelaHistorico.getMapaColuna().get(coluna.getNome());
         
         if (colunaHistorico != null && !colunaHistorico.getTipo().equals(coluna.getTipo())) {
            countProblemaHistorico++;
            verificaNecessidadePreencherBuilder(countProblemaHistorico, historicoSb, strProblemaHistorico);
            historicoSb.append(colunaHistorico.getNome() + " com tipo divergente. " + colunaHistorico.getTipo() + " x " + coluna.getTipo() + "\n");
         } else if (colunaHistorico == null) {
            countProblemaHistorico++;
            verificaNecessidadePreencherBuilder(countProblemaHistorico, historicoSb, strProblemaHistorico);
            historicoSb.append(coluna.getNome() + " inexistente\n");
         }
      }
      
      String strProblemaTabela = "\n* Colunas com problemas na tabela correspondente: " + tabela.getNome() + "\n";
      int countProblemaTabela = 0;
      for (ColunaPdm colunaHistorico : tabelaHistorico.getMapaColuna().values()) {
         
         ColunaPdm coluna = tabelaHistorico.getMapaColuna().get(colunaHistorico.getNome());
         
         if (coluna != null && !coluna.getTipo().equals(coluna.getTipo())) {
            countProblemaTabela++;
            verificaNecessidadePreencherBuilder(countProblemaTabela, historicoSb, strProblemaTabela);
            historicoSb.append(coluna.getNome() + " com tipo divergente. " + coluna.getTipo() + " x " + colunaHistorico.getTipo() + "\n");
         } else if (coluna == null) {
            countProblemaTabela++;
            verificaNecessidadePreencherBuilder(countProblemaTabela, historicoSb, strProblemaTabela);
            historicoSb.append(colunaHistorico.getNome() + " inexistente\n");
         }
      }
   }

   private void verificaNecessidadePreencherBuilder(int count,
      StringBuilder historicoSb, String strProblemaHistorico) {
      if (count == 1) {
         historicoSb.append(strProblemaHistorico);
      }
   }

   private static Map<String, TabelaPdm> montarMapaTabelas(String caminhoPdm) throws DocumentException
   {
      Map<String, TabelaPdm> mapaTabelas = new TreeMap<String, TabelaPdm>();

      SAXReader reader = new SAXReader();
      Document document = reader.read(caminhoPdm);

      Element raiz = document.getRootElement();
      Element rootObject = raiz.element("RootObject");
      Element children = rootObject.element("Children");
      Element model = children.element("Model");
      Element tables = model.element("Tables");

      for (Object obj : tables.elements("Table"))
      {
         Element tabela = (Element) obj;
         Element nomeTabela = tabela.element("Name");
         Element comentarioTabela = tabela.element("Comment");

         TabelaPdm tabelaVo = new TabelaPdm();
         tabelaVo.setNome(nomeTabela.getText().toUpperCase());
         tabelaVo.setPossuiComentario(comentarioTabela != null && StringUtils.isNotBlank(comentarioTabela.getText()));
         tabelaVo.setHistorico(tabelaVo.getNome().startsWith("TH_"));

         Element colunas = tabela.element("Columns");
         
         if (colunas != null) {
            for (Object objColuna : colunas.elements("Column"))
            {
               Element coluna = (Element) objColuna;
               Element nomeColuna = coluna.element("Name");
               Element tipoColuna = coluna.element("DataType");
               Element comentarioColuna = tabela.element("Comment");
   
               ColunaPdm colunaVo = new ColunaPdm();
               colunaVo.setNome(nomeColuna.getText().toUpperCase());
               colunaVo.setPossuiComentario(comentarioColuna != null && StringUtils.isNotBlank(comentarioColuna.getText()));
               
               if (tipoColuna != null && tipoColuna.getText() != null) {
                  colunaVo.setTipo(tipoColuna.getText().toUpperCase());
               } else {
                  colunaVo.setTipo("");
               }
   
               tabelaVo.getMapaColuna().put(colunaVo.getNome(), colunaVo);
            }
         }
         mapaTabelas.put(tabelaVo.getNome(), tabelaVo);
      }
      return mapaTabelas;
   }

}
