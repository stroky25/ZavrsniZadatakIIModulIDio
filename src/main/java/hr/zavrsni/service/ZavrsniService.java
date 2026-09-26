package hr.zavrsni.service;

import hr.zavrsni.dao.PolaznikDao;
import hr.zavrsni.dao.ProgramDao;
import hr.zavrsni.dao.UpisDao;
import hr.zavrsni.model.PolaznikPrograma;

import java.sql.SQLException;
import java.util.List;

public class ZavrsniService {
    private final PolaznikDao polaznikDao = new PolaznikDao();
    private final ProgramDao programDao = new ProgramDao();
    private final UpisDao upisDao = new UpisDao();

    public int unesiPolaznika(String ime, String prezime) throws SQLException {
        return polaznikDao.insert(ime, prezime);
    }

    public int unesiProgram(String naziv, int csvet) throws SQLException {
        return programDao.insert(naziv, csvet);
    }

    public void upisiPolaznika(int polaznikId, int programId) throws SQLException {
        if (polaznikDao.findById(polaznikId).isEmpty())
            throw new IllegalArgumentException("Polaznik ne postoji.");
        if (programDao.findById(programId).isEmpty())
            throw new IllegalArgumentException("Program obrazovanja ne postoji.");
        upisDao.insert(polaznikId, programId);
    }

    public void prebaciPolaznika(int polaznikId, int noviProgramId) throws SQLException {
        if (polaznikDao.findById(polaznikId).isEmpty())
            throw new IllegalArgumentException("Polaznik ne postoji.");
        if (programDao.findById(noviProgramId).isEmpty())
            throw new IllegalArgumentException("Novi program obrazovanja ne postoji.");
        if (!upisDao.hasEnrollment(polaznikId))
            throw new IllegalArgumentException("Polaznik trenutno nije upisan ni na jedan program.");
        upisDao.transfer(polaznikId, noviProgramId);
    }

    public List<PolaznikPrograma> polazniciPrograma(int programId) throws SQLException {
        if (programDao.findById(programId).isEmpty())
            throw new IllegalArgumentException("Program obrazovanja ne postoji.");
        return upisDao.findStudentsByProgram(programId);
    }
}
