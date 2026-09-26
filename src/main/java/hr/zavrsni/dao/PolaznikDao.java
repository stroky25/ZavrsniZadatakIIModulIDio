package hr.zavrsni.dao;

import hr.zavrsni.model.Polaznik;
import java.sql.*;
import java.util.Optional;

public class PolaznikDao {
    public int insert(String ime, String prezime) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_UnesiPolaznika(?, ?)}")) {
            cs.setString(1, ime);
            cs.setString(2, prezime);
            boolean hasResults = cs.execute();
            if (hasResults) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) return rs.getInt("PolaznikID");
                }
            }
            throw new SQLException("Baza nije vratila ID novog polaznika.");
        }
    }

    public Optional<Polaznik> findById(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             CallableStatement cs = c.prepareCall("{call dbo.sp_DohvatiPolaznika(?)}")) {
            cs.setInt(1, id);
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) return Optional.of(new Polaznik(
                        rs.getInt("PolaznikID"), rs.getString("Ime"), rs.getString("Prezime")));
                return Optional.empty();
                }
            }
            return Optional.empty();
        }
    }
}
