package gr.aueb.cf.bankapp.validation;

import gr.aueb.cf.bankapp.dto.AccountInsertDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Validator {

    /**
     * No instances of this class should be available
     */
    private Validator() {

    }

    public static Map<String, String> validate(AccountInsertDTO dto) {
        Map<String, String> errors = new HashMap<>();

        if (dto.getIban() == null || dto.getIban().trim().length() < 5) {
            errors.put("iban", "To iban δεν μπορεί να είναι μικρότερο από 5 ψηφία");
        }

        if (dto.getBalance() == null || dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            errors.put("balance", "Το αρχικό υπόλοιπο ενός λογαριασμού δεν μπορεί να είναι αρνητικό");
        }

//        θα μπορούσαμε να ελέγξουμε αν το iban υπάρχει ήδη.

        return errors;
    }
}