package tp2_dar;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double operande1;
    private String operateur;
    private double operande2;
    
    // Constructeur
    public Operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }
    
    // Getters
    public double getOperande1() { return operande1; }
    public String getOperateur() { return operateur; }
    public double getOperande2() { return operande2; }
    
    // Setters
    public void setOperande1(double operande1) { this.operande1 = operande1; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public void setOperande2(double operande2) { this.operande2 = operande2; }
    
    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
    
    // Méthode de validation simplifiée
    public boolean estValide() {
        try {
            if (operateur == null) return false;
            if (!"+-*/".contains(operateur)) return false;
            if (operateur.equals("/") && operande2 == 0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}