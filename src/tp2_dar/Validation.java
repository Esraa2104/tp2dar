package tp2_dar;

public class Validation {
    
    // Validation simplifiée pour les chaînes de caractères
    public static boolean validerOperationString(String operation) {
        if (operation == null || operation.trim().isEmpty()) return false;
        
        String[] parties = operation.trim().split("\\s+");
        if (parties.length != 3) return false;
        
        try {
            Double.parseDouble(parties[0]);
            Double.parseDouble(parties[2]);
            return "+-*/".contains(parties[1]) && parties[1].length() == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validation pour les objets Operation
    public static boolean validerOperationObjet(Operation operation) {
        if (operation == null) return false;
        if (operation.getOperateur() == null) return false;
        if (!"+-*/".contains(operation.getOperateur())) return false;
        
        // Vérification division par zéro
        if (operation.getOperateur().equals("/") && operation.getOperande2() == 0) {
            return false;
        }
        
        return true;
    }
    
    // Méthode utilitaire pour parser une chaîne en Operation
    public static Operation parserVersOperation(String input) {
        if (!validerOperationString(input)) return null;
        
        try {
            String[] parties = input.trim().split("\\s+");
            double op1 = Double.parseDouble(parties[0]);
            String operateur = parties[1];
            double op2 = Double.parseDouble(parties[2]);
            
            return new Operation(op1, operateur, op2);
        } catch (Exception e) {
            return null;
        }
    }
}
