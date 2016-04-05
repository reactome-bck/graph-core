package org.reactome.server.tools.service;

import org.reactome.server.tools.domain.model.*;
import org.reactome.server.tools.repository.PhysicalEntityRepository;
import org.reactome.server.tools.service.util.DatabaseObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:
 *
 * @author Florian Korninger (florian.korninger@ebi.ac.uk)
 * @since 28.02.16.
 */
@Service
public class PhysicalEntityServiceImpl extends ServiceImpl<PhysicalEntity> implements PhysicalEntityService {

    @Autowired
    private PhysicalEntityRepository physicalEntityRepository;

    @Override
    public GraphRepository<PhysicalEntity> getRepository() {
        return physicalEntityRepository;
    }

    @Override
    public PhysicalEntity findById(String id) {
        id = DatabaseObjectUtils.trimId(id);
        if (DatabaseObjectUtils.isStId(id)) {
            return physicalEntityRepository.findByStableIdentifier(id);
        } else if (DatabaseObjectUtils.isDbId(id)){
            return physicalEntityRepository.findByDbId(Long.parseLong(id));
        }
        return null;
    }

    @Override
    public PhysicalEntity findByDbId(Long dbId) {
        return physicalEntityRepository.findByDbId(dbId);
    }

    @Override
    public PhysicalEntity findByStableIdentifier(String stableIdentifier) {
        return physicalEntityRepository.findByStableIdentifier(stableIdentifier);
    }

    @Override
    @Transactional
    public PhysicalEntity findByIdWithLegacyFields(String id) {
        PhysicalEntity physicalEntity = findById(id);
        if (physicalEntity != null) {
            if (physicalEntity.getCatalystActivities() != null) {
                List<ReactionLikeEvent> catalyzedEvents = new ArrayList<>();
                List<GO_MolecularFunction> goActivities = new ArrayList<>();
                for (CatalystActivity catalystActivity : physicalEntity.getCatalystActivities()) {
                    physicalEntityRepository.findOne(catalystActivity.getId());
                    catalyzedEvents.addAll(catalystActivity.getCatalyzedEvent());
                    goActivities.add(catalystActivity.getActivity());
                }
                physicalEntity.setCatalyzedEvent(catalyzedEvents);
                physicalEntity.setGoActivity(goActivities);
            }
            if (physicalEntity.getIsRequired() != null) {
                List<DatabaseObject> regulatedEntity = new ArrayList<>();
                for (Requirement requirement : physicalEntity.getIsRequired()) {
                    physicalEntityRepository.findOne(requirement.getId());
                    regulatedEntity.add(requirement.getRegulatedEntity());
                }
                physicalEntity.setRequiredEvent(regulatedEntity);
            }
            if (physicalEntity.getPositivelyRegulates() != null) {
                List<DatabaseObject> regulatedEntity = new ArrayList<>();
                for (PositiveRegulation positiveRegulation : physicalEntity.getPositivelyRegulates()) {
                    physicalEntityRepository.findOne(positiveRegulation.getId());
                    regulatedEntity.add(positiveRegulation.getRegulatedEntity());
                }
                physicalEntity.setActivatedEvent(regulatedEntity);
            }
            if (physicalEntity.getNegativelyRegulates() != null) {
                List<DatabaseObject> regulatedEntity = new ArrayList<>();
                for (NegativeRegulation negativeRegulation : physicalEntity.getNegativelyRegulates()) {
                    physicalEntityRepository.findOne(negativeRegulation.getId());
                    regulatedEntity.add(negativeRegulation.getRegulatedEntity());
                }
                physicalEntity.setInhibitedEvent(regulatedEntity);
            }
            return physicalEntity;
        }
        return null;
    }
}