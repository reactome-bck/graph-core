package org.reactome.server.tools.service;

import org.reactome.server.tools.domain.model.Event;

/**
 * Created by:
 *
 * @author Florian Korninger (florian.korninger@ebi.ac.uk)
 * @since 28.02.16.
 */
@SuppressWarnings("SameParameterValue")
public interface EventService extends Service<Event>  {

    @SuppressWarnings("unused")
    Event findById(String id);
    Event findByDbId(Long dbId);
    Event findByStableIdentifier(String stableIdentifier);

    Event findByIdWithLegacyFields(String id);



}