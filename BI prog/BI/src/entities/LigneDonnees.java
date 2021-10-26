/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author MANDOUR FADWA
 */
public class LigneDonnees {
    public static String EVENEMENT = "'Remove From cart'";
    private String idProduct;
    private String evenement;
    private int qt_sup;
    private int cluster;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getEvenement() {
        return evenement;
    }

    public void setEvenement(String evenement) {
        this.evenement = evenement;
    }

    public int getQt_sup() {
        return qt_sup;
    }

    public void setQt_sup(int qt_sup) {
        this.qt_sup = qt_sup;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

          
}
