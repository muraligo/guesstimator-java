package m3.guesstimator.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import m3.guesstimator.model.reference.M3ComponentType;

public class ComponentTypeCollectionResource {
    public String findAll() {
        StringBuilder sb = new StringBuilder("{ \"component_types\": [ ");
        final AtomicBoolean env1st = new AtomicBoolean(true);
        GuesstimatorApplication._componentTypeCache.entrySet().forEach(envent -> {
            if (env1st.get()) {
                env1st.set(false);
            } else {
                sb.append(", ");
            }
            sb.append("{ \"env_name\": ");
            sb.append(envent.getKey());
            sb.append("\"types_by_env\": [ ");
            final AtomicBoolean res1st = new AtomicBoolean(true);
            envent.getValue().entrySet().forEach(resent -> {
                if (res1st.get()) {
                    res1st.set(false);
                } else {
                    sb.append(", ");
                }
                sb.append("{ \"resource_type_name\": ");
                sb.append(resent.getKey());
                sb.append("\"types_by_env\": [ ");
                final AtomicBoolean ct1st = new AtomicBoolean(true);
                resent.getValue().entrySet().forEach(ctent -> {
                    if (ct1st.get()) {
                        ct1st.set(false);
                    } else {
                        sb.append(", ");
                    }
                    M3ComponentType ct = ctent.getValue();
                    sb.append("{ \"component_type_name\": ");
                    sb.append(ct.getName());
                    sb.append(", \"description\": ");
                    sb.append(ct.getDescription());
                    sb.append(", \"component_context\": ");
                    sb.append(ct.getContext().name());
                    sb.append(", \"architectural_layer\": ");
                    sb.append(ct.getArchitecturalLayer().name());
                    sb.append(", \"construct_costs\": ");
                    sb.append(ct.getStrConstructCosts());
                    sb.append(", \"version\": ");
                    sb.append(ct.getVersion());
                    sb.append(" }");
                });
                sb.append(" ] }");
            });
            sb.append(" ] }");
        });
        sb.append(" ] }");
        return sb.toString();
    }
}
