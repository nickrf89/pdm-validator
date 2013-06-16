package org.pdmvalidator.model;

/**
 * ColunaPdm.
 * @author nicolas.ferreira
 */
public class ColunaPdm {

   /** nome. */
   private String nome;

   /** tipo. */
   private String tipo;

   /** lido. */
   private boolean lido;

   private boolean possuiComentario;
   
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
    * ObtÃ©m tipo.
    * @return tipo
    */
   public String getTipo() {
      return tipo;
   }

   /**
    * Define tipo.
    * @param tipo novo tipo
    */
   public void setTipo(String tipo) {
      this.tipo = tipo;
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

}
