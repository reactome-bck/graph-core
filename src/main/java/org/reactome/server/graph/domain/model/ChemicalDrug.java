package org.reactome.server.graph.domain.model;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@NodeEntity
public class ChemicalDrug extends Drug {

    public ChemicalDrug() {
    }

}
