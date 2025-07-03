package gr.aueb.cf.bankapp;

import gr.aueb.cf.bankapp.core.exception.AccountNotFoundException;
import gr.aueb.cf.bankapp.core.exception.InsufficientAmountException;
import gr.aueb.cf.bankapp.core.exception.NegativeAmountException;
import gr.aueb.cf.bankapp.dao.AccountDAOImpl;
import gr.aueb.cf.bankapp.dao.IAccountDAO;
import gr.aueb.cf.bankapp.dto.AccountInsertDTO;
import gr.aueb.cf.bankapp.dto.AccountReadOnlyDTO;
import gr.aueb.cf.bankapp.service.AccountServiceImpl;
import gr.aueb.cf.bankapp.service.IAccountService;
import gr.aueb.cf.bankapp.validation.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private final static IAccountDAO accountDAO = new AccountDAOImpl();
    private final static IAccountService accountService = new AccountServiceImpl(accountDAO);
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String option;
        String iban;

        while (true) {
            printMenu();
            option = scanner.nextLine();

            try {
                switch (option) {
                    case "1":
                        System.out.print("Παρακαλώ εισάγετε το iban: ");
                        iban = scanner.nextLine();
                        System.out.print("Παρακαλώ εισάγετε το υπόλοιπο του λογαριασμού: ");
                        var initialBalance = new BigDecimal(scanner.nextLine());
                        AccountInsertDTO insertDTO = new AccountInsertDTO(iban, initialBalance);

                        // Validation

                        Map<String, String> errors;
                        errors = Validator.validate(insertDTO);

                        if (!errors.isEmpty()) {
                            errors.forEach((k, v) -> System.out.println(v));
                            System.out.print("Ο λογαριασμός δεν δημιουργήθηκε. Προσπαθήστε ξανά.");
                            break;
                        }
                        accountService.createNewAccount(insertDTO);
                        System.out.println("Ο λογαριασμός δημιουργήθηκε επιτυχώς");
                        break;

                    case "2":
                        System.out.print("Παρακαλώ εισάγετε το iban: ");
                        iban = scanner.nextLine();
                        System.out.println("Παρακαλώ εισάγετε το ποσό κατάθεσης: ");
                        var depositAmount = new BigDecimal(scanner.nextLine());

                        accountService.deposit(iban, depositAmount);
                        System.out.println("Η κατάθεση έχει ολοκληρωθεί επιτυχώς");
                        break;
                    case "3":
                        System.out.print("Παρακαλώ εισάγετε το iban: ");
                        iban = scanner.nextLine();
                        System.out.println("Παρακαλώ εισάγετε το ποσό ανάληψης: ");
                        var withdrawAmount = new BigDecimal(scanner.nextLine());

                        accountService.withdraw(iban, withdrawAmount);
                        System.out.println("Η ανάληψη έχει ολοκληρωθεί επιτυχώς");
                        break;
                    case "4":
                        System.out.print("Παρακαλώ εισάγετε το iban: ");
                        iban = scanner.nextLine();

                        BigDecimal balance = accountService.getBalance(iban);
                        System.out.println("Το υπόλοιπο είναι: " + balance);
                        break;
                    case "5":
                        List<AccountReadOnlyDTO> readOnlyDTOS = accountService.getAccounts();
                        if (readOnlyDTOS.isEmpty()) {
                            System.out.println("Δεν βρέθηκαν λογαριασμοί");
                        } else {
                            readOnlyDTOS.forEach(System.out::println);
                        }
                        break;
                    case "6":
                        System.out.println("Έξοδος.......");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Μη έγκυρη επιλογή αριθμού. Προσπαθήστε ξανά.");

                }
            } catch (NegativeAmountException | InsufficientAmountException | AccountNotFoundException e) {
                System.out.println("Λάθος. " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Λάθος μορφή αριθμού. Παρακαλώ εισάγετε ένα έγκυρο ποσό");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\n=== Account Service Menu === ");
        System.out.println("1.Δημιουργία νέου λογαριασμού");
        System.out.println("2.Κατάθεση");
        System.out.println("3.Ανάληψη");
        System.out.println("4.Ερώτηση υπολοίπου");
        System.out.println("5.Εκτύπωση όλων των λογαριασμών");
        System.out.println("6.Έξοδος");
        System.out.print("Εισάγετε μία επιλογή: ");
    }
}
