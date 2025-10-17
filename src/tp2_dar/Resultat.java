package tp2_dar;

import java.io.Serializable;

public class Resultat implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double valeur;
    private String message;
    private boolean succes;
    private Operation operationOriginale;
    
    // Constructeur pour succès
    public Resultat(Operation operation, double valeur) {
        this.operationOriginale = operation;
        this.valeur = valeur;
        this.succes = true;
        this.message = "Opération réussie";
    }
    
    // Constructeur pour erreur
    public Resultat(Operation operation, String messageErreur) {
        this.operationOriginale = operation;
        this.succes = false;
        this.message = messageErreur;
        this.valeur = 0;
    }
    
    // Getters
    public double getValeur() { return valeur; }
    public String getMessage() { return message; }
    public boolean isSucces() { return succes; }
    public Operation getOperationOriginale() { return operationOriginale; }
    
    @Override
    public String toString() {
        if (succes) {
            return operationOriginale + " = " + valeur;
        } else {
            return "ERREUR: " + message + " (opération: " + operationOriginale + ")";
        }
    }
}