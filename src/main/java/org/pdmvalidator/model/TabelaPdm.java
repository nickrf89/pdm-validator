package org.pdmvalidator.model;

import java.util.HashMap;
import java.util.Map;

/**
 * TabelaPdm.
 * @author nicolas.ferreira
 */
public class TabelaPdm {

   /** nome. */
   private String nome;

   /** lido. */
   private boolean lido;

   /** mapa coluna. */
   private Map<String, ColunaPdm> mapaColuna;
   
   private boolean possuiComentario;
   
   private boolean historico;

   /**
    * ObtÃ©m nome.
    * @return nome
    */
   public String getNome() {
      return nome;
   }

   /**
    * Define nome.
    * @param nome novo nome
    */
   public void setNome(String nome) {
      this.nome = nome;
   }

   /**
    * ObtÃ©m mapa coluna.
    * @return mapa coluna
    */
   public Map<String, ColunaPdm> getMapaColuna() {
      if (mapaColuna == null) {
         mapaColuna = new HashMap<String, ColunaPdm>();
      }
      return mapaColuna;
   }

   /**
    * Sets the mapa coluna.
    * @param mapaColuna mapa coluna
    */
   public void setMapaColuna(Map<String, ColunaPdm> mapaColuna) {
      this.mapaColuna = mapaColuna;
   }

   /**
    * Verifica se Ã© lido.
    * @return true, se Ã© lido
    */
   public boolean isLido() {
      return lido;
   }

   /**
    * Define lido.
    * @param lido novo lido
    */
   public void setLido(boolean lido) {
      this.lido = lido;
   }

   public boolean isPossuiComentario() {
      return possuiComentario;
   }
   
   public boolean naoPossuiComentario() {
      return !isPossuiComentario();
   }

   public void setPossuiComentario(boolean possuiComentario) {
      this.possuiComentario = possuiComentario;
   }

   public boolean isHistorico() {
      return historico;
   }

   public void setHistorico(boolean historico) {
      this.historico = historico;
   }

}
