package m3.guesstimator.service;

import javax.persistence.EntityManager;

public interface ApplicationContext {
	EntityManager getEntityManager();
}
