package aplication.dao;

import java.util.List;
import java.util.Optional;

import aplication.model.Rozgrywka;

public interface RozgrywkaDAO {
	Optional<Rozgrywka> findById(Integer rozgrywkaId);
	List<Rozgrywka> findAll();
	void save(Rozgrywka rozgrywka);
	void delateById(Integer rozgrywkaId);
	void delateAll();
	int getLatestId();
}
