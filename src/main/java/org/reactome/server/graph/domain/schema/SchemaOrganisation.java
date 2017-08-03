package org.reactome.server.graph.domain.schema;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.reactome.server.graph.domain.model.Affiliation;

public class SchemaOrganisation extends SchemaCreator {
    private String name;

    SchemaOrganisation(Affiliation affiliation) {
        this.name = affiliation.getDisplayName();
    }

    public String getName() {
        return name;
    }

    @JsonGetter(value = "@type")
    public String getType(){
        return "Organisation";
    }
}
