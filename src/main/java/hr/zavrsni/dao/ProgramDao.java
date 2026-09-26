package hr.zavrsni.dao;

import hr.zavrsni.model.ProgramObrazovanja;
import java.sql.*;
import java.util.Optional;

public class ProgramDao {
    public int insert(String naziv, int csvet) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_UnesiProgram(?, ?)}")) {
            cs.setString(1, naziv);
            cs.setInt(2, csvet);
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) return rs.getInt("ProgramObrazovanjaID");
                }
            }
            throw new SQLException("Baza nije vratila ID novog programa.");
        }
    }

    public Optional<ProgramObrazovanja> findById(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_DohvatiProgram(?)}")) {
            cs.setInt(1, id);
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return Optional.of(new ProgramObrazovanja(
                        rs.getInt("ProgramObrazovanjaID"), rs.getString("Naziv"), rs.getInt("CSVET")));
                return Optional.empty();
                }
            }
            return Optional.empty();
        }
    }
}
