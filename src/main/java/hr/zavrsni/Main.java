package hr.zavrsni;

import hr.zavrsni.model.PolaznikPrograma;
import hr.zavrsni.service.ZavrsniService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final ZavrsniService service = new ZavrsniService();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("=== JavaAdv - Evidencija polaznika ===");
        System.out.println("Provjerite DatabaseConfig.java prije pokretanja.");

        while (true) {
            printMenu();
            int izbor = readInt("Odaberite opciju: ");

            try {
                switch (izbor) {
                    case 1 -> unesiPolaznika();
                    case 2 -> unesiProgram();
                    case 3 -> upisiPolaznika();
                    case 4 -> prebaciPolaznika();
                    case 5 -> prikaziPolaznikePrograma();
                    case 0 -> {
                        System.out.println("Kraj programa.");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Nepoznata opcija.");
                }
            } catch (SQLException e) {
                System.out.println("Greška baze: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Greška: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Neočekivana greška: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1 - Unesi novog polaznika");
        System.out.println("2 - Unesi novi program obrazovanja");
        System.out.println("3 - Upiši polaznika na program obrazovanja");
        System.out.println("4 - Prebaci polaznika iz jednog u drugi program obrazovanja");
        System.out.println("5 - Prikaži polaznike za program");
        System.out.println("0 - Izlaz");
    }

    private void unesiPolaznika() throws SQLException {
        String ime = readText("Ime: ");
        String prezime = readText("Prezime: ");
        int id = service.unesiPolaznika(ime, prezime);
        System.out.println("Polaznik je spremljen. ID = " + id);
    }

    private void unesiProgram() throws SQLException {
        String naziv = readText("Naziv programa: ");
        int csvet = readPositiveInt("CSVET bodovi: ");
        int id = service.unesiProgram(naziv, csvet);
        System.out.println("Program je spremljen. ID = " + id);
    }

    private void upisiPolaznika() throws SQLException {
        int polaznikId = readPositiveInt("ID polaznika: ");
        int programId = readPositiveInt("ID programa obrazovanja: ");
        service.upisiPolaznika(polaznikId, programId);
        System.out.println("Polaznik je upisan na program.");
    }

    private void prebaciPolaznika() throws SQLException {
        int polaznikId = readPositiveInt("ID polaznika: ");
        int noviProgramId = readPositiveInt("ID novog programa: ");
        service.prebaciPolaznika(polaznikId, noviProgramId);
        System.out.println("Polaznik je uspješno prebačen. Transakcija je potvrđena.");
    }

    private void prikaziPolaznikePrograma() throws SQLException {
        int programId = readPositiveInt("ID programa obrazovanja: ");
        List<PolaznikPrograma> polaznici = service.polazniciPrograma(programId);

        if (polaznici.isEmpty()) {
            System.out.println("Na tom programu nema upisanih polaznika.");
            return;
        }

        System.out.printf("%-20s %-20s %-35s %-8s%n",
                "Ime", "Prezime", "Program", "CSVET");
        System.out.println("-".repeat(90));

        for (PolaznikPrograma p : polaznici) {
            System.out.printf("%-20s %-20s %-35s %-8d%n",
                    p.ime(), p.prezime(), p.nazivPrograma(), p.csvet());
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Vrijednost je obavezna.");
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Unesite broj veći od 0.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Unesite cijeli broj.");
            }
        }
    }
}
