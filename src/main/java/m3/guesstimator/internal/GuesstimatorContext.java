package m3.guesstimator.internal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import m3.guesstimator.service.ApplicationContext;

class GuesstimatorContext implements ApplicationContext {
	private static final String PERSISTENCE_UNIT_NAME = "guesstimator-java";
	private static EntityManagerFactory factory;

	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		if (em == null) {
			em = factory.createEntityManager();
		}
		return em;
	}

	public void releaseEntityManager() {
		if (em != null) {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.flush();
			}
			em.clear();
			em.close();
		}
	}

	void initialize() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}
}
