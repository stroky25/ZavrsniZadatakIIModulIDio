package hr.zavrsni.dao;

import hr.zavrsni.model.PolaznikPrograma;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpisDao {
    public void insert(int idPolaznik, int idProgram) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_UpisiPolaznika(?, ?)}")) {
            cs.setInt(1, idPolaznik);
            cs.setInt(2, idProgram);
            cs.execute();
        }
    }

    public boolean hasEnrollment(int idPolaznik) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_DohvatiUpisPolaznika(?)}")) {
            cs.setInt(1, idPolaznik);
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    return rs.next();
                }
            }
            return false;
        }
    }

    public void transfer(int idPolaznik, int noviProgram) throws SQLException {
        // Jedna Connection + jedna transakcija za obje pohranjene procedure.
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (CallableStatement delete = c.prepareCall("{call dbo.sp_ObrisiUpis(?)}")) {
                    delete.setInt(1, idPolaznik);
                    delete.execute();
                }

                try (CallableStatement insert = c.prepareCall("{call dbo.sp_DodajUpis(?, ?)}")) {
                    insert.setInt(1, idPolaznik);
                    insert.setInt(2, noviProgram);
                    insert.execute();
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<PolaznikPrograma> findStudentsByProgram(int programId) throws SQLException {
        List<PolaznikPrograma> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_PolazniciPrograma(?)}")) {
            cs.setInt(1, programId);
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        result.add(new PolaznikPrograma(
                                rs.getString("Ime"),
                                rs.getString("Prezime"),
                                rs.getString("Naziv"),
                                rs.getInt("CSVET")
                        ));
                    }
                }
            }
        }
        return result;
    }
}
