package aplication.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import aplication.datasource.DataSource;
import aplication.expection.DBExpection;
import aplication.model.OXEnum;
import aplication.model.Rozgrywka;

public class RozgrywkaDOAImpt implements RozgrywkaDAO {

	@Override
	public Optional<Rozgrywka>  findById(Integer rozgrywkaId) {
	    String query = "SELECT * FROM rozgrywka WHERE rozgrywka_id = ?";
	    Rozgrywka rozgrywka = null;

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement stmt = connect.prepareStatement(query)) {
	        stmt.setInt(1, rozgrywkaId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                String graczO = rs.getString("gracz_o");
	                String graczX = rs.getString("gracz_x");
	                OXEnum zwyciezca = OXEnum.fromString(rs.getString("zwyciezca"));
	                LocalDateTime dataczasRozgrywki = 
	                    rs.getObject("dataczas_rozgrywki", LocalDateTime.class);

	                rozgrywka = new Rozgrywka(
	                    rozgrywkaId, graczX, graczO, zwyciezca, dataczasRozgrywki
	                );
	            }
	        }

	    } catch (SQLException e) {
	        throw new DBExpection("Błąd podczas pobierania rozgrywki z bazy danych!", e);
	    }

	    return Optional.ofNullable(rozgrywka);
	}


	@Override
	public List<Rozgrywka> findAll() {
	    List<Rozgrywka> rozgrywki = new ArrayList<>();
	    String query = "SELECT * FROM rozgrywka ORDER BY dataczas_rozgrywki DESC";
	    try (Connection connect = DataSource.getConnection();
	         Statement stmt = connect.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {
	        while (rs.next()) {
	            Integer rozgrywkaId = rs.getInt("rozgrywka_id");
	            String graczO = rs.getString("gracz_o");
	            String graczX = rs.getString("gracz_x");
	            String zwyciezcaStr = rs.getString("zwyciezca");
	            OXEnum zwyciezca = null;
	            try {
	                zwyciezca = OXEnum.fromString(zwyciezcaStr);
	            } catch (IllegalArgumentException e) {
	            	String.format("Niepoprawna wartość zwyciezcy: " + zwyciezcaStr, e);
	                zwyciezca = OXEnum.NONE; // Ustaw domyślną wartość w przypadku błędu
	            }
	            LocalDateTime dataczasRozgrywki = rs.getObject("dataczas_rozgrywki", LocalDateTime.class);
	            Rozgrywka rozgrywka = new Rozgrywka(rozgrywkaId, graczX, graczO, zwyciezca, dataczasRozgrywki);
	            rozgrywki.add(rozgrywka);
	        }
	    } catch (SQLException e) {
	        throw new DBExpection("Błąd podczas pobierania rozgrywki z bazy danych!", e);
	    }
	    return rozgrywki;
	}

	@Override
	public void save(Rozgrywka rozgrywka ) {
		String query = "INSERT INTO rozgrywka(gracz_o, gracz_x, zwyciezca, dataczas_rozgrywki) Values (?,?,?,?)";
		try(Connection connect = DataSource.getConnection();
				PreparedStatement preparedStmt = 
						connect.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){
			preparedStmt.setString(1,rozgrywka.getGraczO());
			preparedStmt.setString(2,rozgrywka.getGraczX());
			preparedStmt.setString(3,rozgrywka.getZwyciezca().toString());
			preparedStmt.setObject(4,rozgrywka.getDataczasRozgrywki());
			preparedStmt.executeUpdate();
			try (ResultSet rs = preparedStmt.getGeneratedKeys()){
				if (rs.next())
					rozgrywka.setRozgrywkaId(rs.getInt(1));
			}
		}
	catch(SQLException e) {
					throw new  DBExpection("Błąd podczas zapisu rozgrywki w bazie danych!",e);
				}
	}

	@Override
    public void delateById(Integer rozgrywkaId) {
        String query = "DELETE FROM rozgrywka WHERE rozgrywka_id = ?";
        try (Connection connect = DataSource.getConnection();
             PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setInt(1, rozgrywkaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBExpection("Błąd podczas usuwania rozgrywki z bazy danych!", e);
        }
    }

	@Override
    public void delateAll() {
        String query = "DELETE FROM rozgrywka";
        try (Connection connect = DataSource.getConnection();
             Statement stmt = connect.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new DBExpection("Błąd podczas usuwania wszystkich rozgrywek z bazy danych!", e);
        }
    }
	@Override
	public int getLatestId() throws DBExpection {
	    String sql = "SELECT MAX(rozgrywka_id) AS latestId FROM rozgrywka";
	    try (Connection connection = DataSource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        if (resultSet.next()) {
	            return resultSet.getInt("latestId");
	        } else {
	            throw new DBExpection("No data found in the table.");
	        }

	    } catch (SQLException e) {
	        throw new DBExpection("Error retrieving latest ID", e);
	    }
	}
}
