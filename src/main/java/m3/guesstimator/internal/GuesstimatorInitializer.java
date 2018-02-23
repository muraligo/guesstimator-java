package m3.guesstimator.internal;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m3.guesstimator.internal.data.EstimatorComponentDao;
import m3.guesstimator.internal.data.EstimatorComponentTypeDao;
import m3.guesstimator.model.reference.ComponentContext;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.model.reference.Layer;
import m3.guesstimator.service.ApplicationContext;

class GuesstimatorInitializer {
    ApplicationContext ctx;
    GuesstimatorApplication app;

    GuesstimatorInitializer(GuesstimatorApplication app) {
        this.app = app;
    }

    void initializeDao() {
        EstimatorComponentTypeDao ctypedao = new EstimatorComponentTypeDao();
        GuesstimatorApplication._daoMap.put("component_type", ctypedao);
        EstimatorComponentDao compdao = new EstimatorComponentDao();
        GuesstimatorApplication._daoMap.put("component", compdao);
        // TODO Same with other daos and then resources
        // for resources we have 2 types of keys <resource_name>.single and <resource_name>.collection
    }

    void initializeComponentType(EstimatorComponentTypeDao dao) {
//		em.getTransaction().begin();
    	dao.removeAll();

		// DevEnv subsystem
        persistComponentType("Dev.Physical.Run-Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
				, dao);
		persistComponentType("Dev.Physical.Run-Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
                , dao);
		persistComponentType("Dev.Physical.Build-Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.Physical.Build-Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);

		persistComponentType("Dev.Tools.SourceRepository", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.Tools.ArtifactRepository", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.Tools.ALM", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.Tools.BuildTool", "acquisition and configuration (Ant, Maven, Make, etc)", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]"
		        , dao);
		persistComponentType("Dev.Tools.IDE", "acquisition and configuration (Eclipse, JDeveloper, etc)", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]"
		        , dao);

		persistComponentType("Dev.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]"
                , dao);
		persistComponentType("Dev.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]"
		        , dao);

		persistComponentType("Dev.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 15}, {\"phase\": \"Test\", \"value\": 5} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]"
		        , dao);
		persistComponentType("Dev.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]"
		        , dao);

//		em.getTransaction().commit();
//		em.getTransaction().begin();

		// QAEnv subsystem
		persistComponentType("QA.Physical.Run-Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 30}, {\"phase\": \"Test\", \"value\": 15} ]", 
                dao);
		persistComponentType("QA.Physical.Run-Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 30}, {\"phase\": \"Test\", \"value\": 15} ]", 
                dao);
		persistComponentType("QA.Tools.FunctionalTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("QA.Tools.BugTrackingTool", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("QA.Tools.ALM", "acquisition and configuration - only tools like Sonar or some such that show quality from automated tests", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("QA.Data.FunctionalTestData", "acquisition and configuration", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);

		persistComponentType("QA.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]",
		        dao);
		persistComponentType("QA.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]",
		        dao);

		persistComponentType("QA.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]",
		        dao);
		persistComponentType("QA.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]",
		        dao);
		persistComponentType("QA.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]",
		        dao);
		persistComponentType("QA.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
		        dao);
		persistComponentType("QA.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]",
		        dao);

//		em.getTransaction().commit();
//		em.getTransaction().begin();

		// Performance/Security Env subsystem
		// like QA but with Security set up at full strength like Prod and Physical set up like Prod
		persistComponentType("Perf.Physical.Run-Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]",
				dao);
		persistComponentType("Perf.Physical.Run-Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]",
				dao);
		persistComponentType("Perf.Tools.PerformanceTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]",
				dao);
		persistComponentType("Perf.Tools.SecurityTestTools", "installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]",
				dao);
		persistComponentType("Perf.Tools.ALM", "acquisition and configuration - only tools like Sonar or some such that show quality from automated tests", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);
		persistComponentType("Perf.Data.PerformanceTestData", "acquisition and configuration (includes security threat matrix)", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);

		persistComponentType("Perf.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);
		persistComponentType("Perf.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]",
				dao);

		persistComponentType("Perf.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);
		persistComponentType("Perf.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);
		persistComponentType("Perf.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]",
				dao);
		persistComponentType("Perf.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]",
				dao);
		persistComponentType("Perf.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]",
				dao);

		// Could also have a Partner Integration Env for testing integrations with external partners

//		em.getTransaction().commit();
//		em.getTransaction().begin();

		// ProductionEnv subsystem
		persistComponentType("Prod.Physical.Run.Compute", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]", 
                dao);
		persistComponentType("Prod.Physical.Run.Storage", "acquisition and initialization of environment", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 20}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]", 
                dao);
		persistComponentType("Prod.Security.KeyStore", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.Security.Certificate", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 4}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.Security.AAA", "provisioning", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("Prod.Security.Connectivity", "provisioning", 
				ComponentContext.DevOps, Layer.Infrastructure, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 35}, {\"phase\": \"Test\", \"value\": 20} ]", 
                dao);

		persistComponentType("Prod.AppFramework.JEEServer", "per application type - installation and configuration - Web or SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("Prod.AppFramework.SOASuite", "per application type - installation and configuration - SoaSuite with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("Prod.AppFramework.Spring", "per application type - installation and configuration - Web or Spring with Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("Prod.AppFramework.JSE", "per application type - installation and configuration - Plain Java", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.AppFramework.Python", "per application type - installation and configuration - Plain Python", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.AppFramework.PHP", "per application type - installation and configuration - Web or Plain with PHP", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.AppFramework.Go", "per application type - installation and configuration - Web or Plain with Go", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("Prod.AppFramework.DataStoresSW", "per application type - installation and configuration", 
				ComponentContext.DevOps, Layer.Technical, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);

//		em.getTransaction().commit();
//		em.getTransaction().begin();

		// Application Schema
		persistComponentType("General.Schema.RDBMS_database", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 5}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("General.Schema.RDBMS_schema", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 25}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);
		persistComponentType("General.Schema.RDBMS_table", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Schema.RDBMS_index", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("General.Schema.RDBMS_relations", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]", 
                dao);
		persistComponentType("General.Schema.NoSQL_database", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("General.Schema.NoSQL_schema", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 2} ]", 
                dao);
		persistComponentType("General.Schema.NoSQL_table", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Schema.NoSQL_index", "data model definition", 
				ComponentContext.DevOps, Layer.Information, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);

		// Application Components
		persistComponentType("General.Code.DataObject", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 1}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 1} ]", 
                dao);
		persistComponentType("General.Code.DataQuery", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]", 
                dao);
		persistComponentType("General.Code.TransferObject", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 1}, {\"phase\": \"Design\", \"value\": 1}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 1} ]", 
                dao);
		persistComponentType("General.Code.DataService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 3}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Code.BusinessLogic", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 3}, {\"phase\": \"Test\", \"value\": 3} ]", 
                dao);
		persistComponentType("General.Code.BusinessService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 3}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Code.FrameworkService", "code", 
				ComponentContext.DevOps, Layer.Application, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 10}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Code.PresentationWidget", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 2}, {\"phase\": \"Design\", \"value\": 3}, "
						+ " {\"phase\": \"Build\", \"value\": 2}, {\"phase\": \"Test\", \"value\": 3} ]", 
                dao);
		persistComponentType("General.Code.PresentationScreen", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Code.PresentationFlow", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 4}, {\"phase\": \"Design\", \"value\": 7}, "
						+ " {\"phase\": \"Build\", \"value\": 4}, {\"phase\": \"Test\", \"value\": 5} ]", 
                dao);
		persistComponentType("General.Code.PresentationLnF", "code", 
				ComponentContext.DevOps, Layer.Presentation, 
				"[ {\"phase\": \"Analysis\", \"value\": 5}, {\"phase\": \"Design\", \"value\": 10}, "
						+ " {\"phase\": \"Build\", \"value\": 5}, {\"phase\": \"Test\", \"value\": 10} ]", 
                dao);

//		em.getTransaction().commit();
//		em.close();
	}

    private M3ComponentType createComponentType(String name, String desc, ComponentContext compCtx, Layer layer, String costs) {
        M3ComponentType t = new M3ComponentType();
        t.setName(name);
        t.setDescription(desc);
        t.setStrConstructCosts(costs);
        t.setContext(compCtx);
        t.setArchitecturalLayer(layer);
        return t;
    }

    private void persistComponentType(String name, String desc, ComponentContext compCtx, Layer layer, String costs, EstimatorComponentTypeDao dao) {
        M3ComponentType ct = createComponentType(name, desc, compCtx, layer, costs);
        // em.persist(ct);
        M3ComponentType ct2 = dao.put(ct);
        String[] name_parts = ct2.getName().split(".");
        Map<String, Map<String, M3ComponentType>> env = GuesstimatorApplication._componentTypeCache.get(name_parts[0]);
        if (env == null) {
            env = Collections.synchronizedMap(new ConcurrentHashMap<String, Map<String, M3ComponentType>>(20));
            GuesstimatorApplication._componentTypeCache.put(name_parts[0], env);
        }
        Map<String, M3ComponentType> resource_type = env.get(name_parts[1]);
        if (resource_type == null) {
            resource_type = Collections.synchronizedMap(new ConcurrentHashMap<String, M3ComponentType>(20));
            env.put(name_parts[1], resource_type);
        }
        resource_type.put(ct2.getName(), ct2);
   }
}
