package kz.iitu.projects.zoomedcenter.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kz.iitu.projects.zoomedcenter.model.Specialty;
import kz.iitu.projects.zoomedcenter.repository.SpecialtyRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public class JpaSpecialtyRepositoryImpl implements SpecialtyRepository {
	
    @PersistenceContext
    private EntityManager em;

	@Override
	public Specialty findById(int id) {
		return this.em.find(Specialty.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Specialty> findAll() throws DataAccessException {
		return this.em.createQuery("SELECT s FROM Specialty s").getResultList();
	}

	@Override
	public void save(Specialty specialty) throws DataAccessException {
		if (specialty.getId() == null) {
            this.em.persist(specialty);
        } else {
            this.em.merge(specialty);
        }
	}

	@Override
	public void delete(Specialty specialty) throws DataAccessException {
		this.em.remove(this.em.contains(specialty) ? specialty : this.em.merge(specialty));
		String specId = specialty.getId().toString();
		this.em.createNativeQuery("DELETE FROM vet_specialties WHERE specialty_id=" + specId).executeUpdate();
		this.em.createQuery("DELETE FROM Specialty specialty WHERE id=" + specId).executeUpdate();
	}

}
