package m3.guesstimator.internal;

import javax.persistence.EntityManager;

import m3.guesstimator.model.reference.ComponentContext;
import m3.guesstimator.model.reference.ComponentType;
import m3.guesstimator.model.reference.Layer;
import m3.guesstimator.service.ApplicationContext;

class GuesstimatorInitializer {
	ApplicationContext ctx;

	void initializeComponentType() {
		ComponentType ctype;
		EntityManager em = ctx.getEntityManager();
		em.getTransaction().begin();

		// DevEnv subsystem
		ctype = createComponentType("Dev.Physical.Run.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Physical.Run.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Physical.Build.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Physical.Build.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);

		ctype = createComponentType("Dev.Tools.SourceRepository", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Tools.ArtifactRepository", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Tools.ALM", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Tools.BuildTool", "acquisition and configuration (Ant, Maven, Make, etc)", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Tools.IDE", "acquisition and configuration (Eclipse, JDeveloper, etc)", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		ctype = createComponentType("Dev.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);

		ctype = createComponentType("Dev.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 15}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Dev.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		em.getTransaction().commit();
		em.getTransaction().begin();

		// QAEnv subsystem
		ctype = createComponentType("QA.Physical.Run.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 30}, {\"phase\": \"Test\", \"value\": 15} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Physical.Run.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 30}, {\"phase\": \"Test\", \"value\": 15} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Tools.FunctionalTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Tools.BugTrackingTool", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Tools.ALM", "acquisition and configuration - only tools like Sonar or some such that show quality from automated tests", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Data.FunctionalTestData", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);

		ctype = createComponentType("QA.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);

		ctype = createComponentType("QA.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("QA.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		em.getTransaction().commit();
		em.getTransaction().begin();

		// Performance/Security Env subsystem
		// like QA but with Security set up at full strength like Prod and Physical set up like Prod
		ctype = createComponentType("Perf.Physical.Run.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Physical.Run.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Tools.PerformanceTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Tools.SecurityTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Tools.ALM", "acquisition and configuration - only tools like Sonar or some such that show quality from automated tests", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Data.PerformanceTestData", "acquisition and configuration (includes security threat matrix)", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);

		ctype = createComponentType("Perf.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);

		ctype = createComponentType("Perf.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Perf.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		// Could also have a Partner Integration Env for testing integrations with external partners

		em.getTransaction().commit();
		em.getTransaction().begin();

		// ProductionEnv subsystem
		ctype = createComponentType("Prod.Physical.Run.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.Physical.Run.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]");
		em.persist(ctype);

		ctype = createComponentType("Prod.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("Prod.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		em.getTransaction().commit();
		em.getTransaction().begin();

		// Application Schema
		ctype = createComponentType("General.Schema.RDBMS_database", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.RDBMS_schema", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.RDBMS_table", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.RDBMS_index", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.RDBMS_relations", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.NoSQL_database", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.NoSQL_schema", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.NoSQL_table", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Schema.NoSQL_index", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);

		// Application Components
		ctype = createComponentType("General.Code.DataObject", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 1}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 1} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.DataQuery", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.TransferObject", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 1}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 1} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.DataService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 3}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.BusinessLogic", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 3} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.BusinessService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 3}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.FrameworkService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.PresentationWidget", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.PresentationScreen", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.PresentationFlow", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 5} ]");
		em.persist(ctype);
		ctype = createComponentType("General.Code.PresentationLnF", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]");
		em.persist(ctype);

		em.getTransaction().commit();
		em.close();
	}

	private ComponentType createComponentType(String name, String desc, ComponentContext compCtx, Layer layer, String costs) {
		ComponentType t = new ComponentType();
		t.setName(name);
		t.setDescription(desc);
		t.setStrConstructCosts(costs);
		t.setContext(compCtx);
		t.setArchitecturalLayer(layer);
		return t;
	}
}
