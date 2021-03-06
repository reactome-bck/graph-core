package org.reactome.server.graph.domain.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * The name of the has been taken from Cytoscape.
 *
 * Directed interactions has two properties holding a ReferenceEntity instance each. The interaction is
 *
 *   (source) -[interacts]-> (target)
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("unused")
@NodeEntity
public class DirectedInteraction extends Interaction {

    @Relationship(type = "source")
    private ReferenceEntity source;

    @Relationship(type = "target")
    private ReferenceEntity target;

    public DirectedInteraction() { }

    public ReferenceEntity getSource() {
        return source;
    }

    @Relationship(type = "source")
    public void setSource(ReferenceEntity source) {
        this.source = source;
    }

    public ReferenceEntity getTarget() {
        return target;
    }

    @Relationship(type = "target")
    public void setTarget(ReferenceEntity target) {
        this.target = target;
    }
}
