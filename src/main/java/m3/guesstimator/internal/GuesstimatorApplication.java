package m3.guesstimator.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

import static spark.Spark.get;
import static spark.Spark.post;

import m3.guesstimator.internal.data.AbstractDao;
import m3.guesstimator.internal.data.EstimatorComponentDao;
import m3.guesstimator.internal.data.EstimatorComponentTypeDao;
import m3.guesstimator.model.functional.M3Component;
import m3.guesstimator.model.reference.M3ComponentType;
import m3.guesstimator.service.ApplicationContext;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class GuesstimatorApplication {
    GuesstimatorContext ctx;
    GuesstimatorInitializer initializer;
    static final Map<String, Class<?>> _resourceMap = Collections.synchronizedMap(
            new ConcurrentHashMap<String, Class<?>>(40));
    static final Map<String, AbstractDao> _daoMap = Collections.synchronizedMap(
            new ConcurrentHashMap<String, AbstractDao>(40));
    static final Map<String, Map<String, Map<String, M3ComponentType>>> _componentTypeCache = Collections.synchronizedMap(
            new ConcurrentHashMap<String, Map<String, Map<String, M3ComponentType>>>(20));

    public GuesstimatorApplication(ApplicationContext ctx) {
        super();
        this.ctx = (GuesstimatorContext)ctx;
        initializer = new GuesstimatorInitializer(this);
        initializer.initializeDao();
        initializer.initializeComponentType((EstimatorComponentTypeDao) _daoMap.get("component_type"));

        // ******** verbs for component type resource **********
        get("/component_types", (req, res) -> {
            ComponentTypeCollectionResource resource = new ComponentTypeCollectionResource();
            // retrieve all from cache and so get a JSON
            String result = resource.findAll();
            HashMap<String, Object> model = new HashMap<String, Object>(1);
            model.put("compTypeTableData", result);
            ModelAndView mv = new ModelAndView(model, "/component_types/");
//            res.status(200);
//            res.type("application/json");
            return new HandlebarsTemplateEngine().render(mv);
        });

        // ******** verbs for component resource **********
        post("/new/component", (req, res) -> {
            Gson gson = new Gson();
            M3Component ent = gson.fromJson(req.body(), M3Component.class);
            EstimatorComponentDao dao = (EstimatorComponentDao) _daoMap.get("component");
            ComponentResource resource = new ComponentResource();
            resource.dao = dao;
            EstimatorResponse<M3Component> resp = resource.store(ent);
            // Handle errors
            if (resp.status() != 200) {
                String errjson = resp.error();
                // TODO put error in response
            } else {
                // TODO convert results to json and put in response
            }
            // TODO ideally following should not be here and the resp is returned and the Transformers convert them
            res.status(resp.status());
            res.type("text/plain"); // TODO actually pull from res
            return "inserted"; // TODO actually return a new value or something like that
        });
        get("/components/", (req, res) -> {
            EstimatorComponentDao dao = (EstimatorComponentDao) _daoMap.get("component");
            ComponentCollectionResource resource = new ComponentCollectionResource();
            resource.dao = dao;
            EstimatorResponse<M3Component> resp = resource.retrieveAll();
            ModelAndView mv = null;
            // Handle errors
            if (resp.status() == 200) {
                HashMap<String, Object> model = ComponentCollectionResource.toTableAndJsonWithIndex(resp);
                mv = new ModelAndView(model, "/components/");
            } else {
                String errjson = resp.error();
                HashMap<String, Object> model = new HashMap<String, Object>(1);
                model.put("errorJson", errjson);
                mv = new ModelAndView(model, "/errors/");
            }
            return new HandlebarsTemplateEngine().render(mv);
        });
	}

}
