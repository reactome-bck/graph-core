package uk.ac.ebi.reactome.qualityassurance.tests;

import uk.ac.ebi.reactome.qualityassurance.QATest;

/**
 * Created by:
 *
 * @author Florian Korninger (florian.korninger@ebi.ac.uk)
 * @since 07.03.16.
 */
@SuppressWarnings("unused")
@QATest
public class QualityAssuranceTest003 extends QualityAssuranceAbstract {

    @Override
    String getName() {
        return "DatabaseIdentifierWithoutIdentifier";
    }

    @Override
    String getQuery() {
        return "Match (n:DatabaseIdentifier)<-[:created]-(a) Where n.identifier is NULL RETURN n.dbId AS dbId, " +
                "n.stableIdentifier AS stId, n.displayName AS name, a.displayName as author";
    }
}