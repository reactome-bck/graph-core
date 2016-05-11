package org.reactome.server.graph.service;

import com.google.common.collect.Iterables;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactome.server.graph.config.Neo4jConfig;
import org.reactome.server.graph.domain.model.*;
import org.reactome.server.graph.domain.result.SchemaClassCount;
import org.reactome.server.graph.domain.result.SimpleDatabaseObject;
import org.reactome.server.graph.service.helper.RelationshipDirection;
import org.reactome.server.graph.util.DatabaseObjectFactory;
import org.reactome.server.graph.util.JunitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 * Created by:
 *
 * @author Florian Korninger (florian.korninger@ebi.ac.uk)
 * @since 25.01.16.
 */
@ContextConfiguration(classes = { Neo4jConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class GeneralServiceTest {

    private static final Logger logger = LoggerFactory.getLogger("testLogger");

    private static Boolean checkedOnce = false;
    private static Boolean isFit = false;

    private static final Long dbId = 5205685L;
    private static final Long dbId2 = 199420L;
    private static final String stId = "R-HSA-5205685";
    private static final String stId2 = "R-HSA-199420";

    @Autowired
    private GeneralService generalService;

    @BeforeClass
    public static void setUpClass() {
        logger.info(" --- !!! Running DatabaseObjectServiceTests !!! --- \n");
    }

    @AfterClass
    public static void tearDownClass() {
        logger.info("\n\n");
    }

    @Before
    public void setUp() throws Exception {
        if (!checkedOnce) {
            isFit = generalService.fitForService();
            checkedOnce = true;
        }
        assumeTrue(isFit);
        generalService.clearCache();
        DatabaseObjectFactory.clearCache();
    }

    // Find by property and depth

    @Test
    public void findByPropertyTest() throws InvocationTargetException, IllegalAccessException {

        logger.info("Started testing genericService.findByProperty");
        long start, time;
        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = generalService.findByProperty(DatabaseObject.class, "stableIdentifier", stId, 1);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        JunitHelper.assertDatabaseObjectsEqual(databaseObjectExpected, databaseObjectObserved);
        logger.info("Finished");
    }

//    @Test
//    todo Method is currently broken report to SDN
    public void findByPropertiesTest() {

        logger.info("Started testing genericService.findByProperties");
        long start, time;
        start = System.currentTimeMillis();
        Collection<DatabaseObject> databaseObjectsObserved = generalService.findByProperties(DatabaseObject.class, "stableIdentifier", Arrays.<Object>asList(stId, stId2), 0);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, databaseObjectsObserved.size());
        logger.info("Finished");
    }

    // Default Finder Methods

    @Test
    public void findByDbIdWithClassTest() throws InvocationTargetException, IllegalAccessException {
        logger.info("Started testing genericService.findByDbIdWithClass");
        long start, time;
        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = generalService.findByDbIdNoRelations(DatabaseObject.class, dbId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(databaseObjectExpected.getDbId(), databaseObjectObserved.getDbId());
        assertEquals(databaseObjectExpected.getStableIdentifier(), databaseObjectObserved.getStableIdentifier());
        assertEquals(databaseObjectExpected.getDisplayName(), databaseObjectObserved.getDisplayName());
        logger.info("Finished");
    }

    @Test
    public void findByStableIdentifierWithClassTest() throws InvocationTargetException, IllegalAccessException {
        logger.info("Started testing genericService.findByStableIdentifierWithClass");
        long start, time;
        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = generalService.findByStableIdentifierNoRelations(DatabaseObject.class, stId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(databaseObjectExpected.getDbId(), databaseObjectObserved.getDbId());
        assertEquals(databaseObjectExpected.getStableIdentifier(), databaseObjectObserved.getStableIdentifier());
        assertEquals(databaseObjectExpected.getDisplayName(), databaseObjectObserved.getDisplayName());
        logger.info("Finished");
    }

    @Test
    public void findByDbIdsWithClassTest() {

        logger.info("Started testing genericService.findByDbIdsWithClass");
        long start, time;
        start = System.currentTimeMillis();
        Iterable<DatabaseObject> databaseObjectsObserved = generalService.findByDbIdsNoRelations(DatabaseObject.class, Arrays.<Long>asList(dbId, dbId2));
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, Iterables.size(databaseObjectsObserved));
        logger.info("Finished");
    }

    @Test
    public void findByStableIdentifiersWithClassTest() {

        logger.info("Started testing genericService.findByStableIdentifiersWithClass");
        long start, time;
        start = System.currentTimeMillis();
        Iterable<DatabaseObject> databaseObjectsObserved = generalService.findByStableIdentifiersNoRelations(DatabaseObject.class, Arrays.<String>asList(stId, stId2));
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, Iterables.size(databaseObjectsObserved));
        logger.info("Finished");
    }

    // Undirected will load all relationships todo: modify test
    @Test
    public void findByDbIdWithRelationshipDirectionTest() throws InvocationTargetException, IllegalAccessException {
        logger.info("Started testing genericService.findByDbIdWithRelationshipDirection");
        long start, time;
        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = generalService.findByDbId(dbId, RelationshipDirection.UNDIRECTED);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        JunitHelper.assertDatabaseObjectsEqual(databaseObjectExpected, databaseObjectObserved);
        logger.info("Finished");
    }

    // Undirected will load all relationships todo: modify test
    @Test
    public void findByStableIdentifierWithRelationshipDirectionTest() throws InvocationTargetException, IllegalAccessException {
        logger.info("Started testing genericService.findByStableIdentifierWithRelationshipDirection");
        long start, time;
        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = generalService.findByStableIdentifier(stId, RelationshipDirection.UNDIRECTED);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        JunitHelper.assertDatabaseObjectsEqual(databaseObjectExpected, databaseObjectObserved);
        logger.info("Finished");
    }

    @Test
    public void findByDbIdsWithRelationshipDirectionTest() {
        logger.info("Started testing genericService.findByDbIdWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Collection<DatabaseObject> databaseObjectObserved = generalService.findByDbIds(Arrays.<Long>asList(dbId, dbId2), RelationshipDirection.OUTGOING);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, databaseObjectObserved.size());
        logger.info("Finished");
    }

    @Test
    public void findByStableIdentifiersWithRelationshipDirectionTest() {
        logger.info("Started testing genericService.findByStableIdentifierWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Collection<DatabaseObject> databaseObjectObserved = generalService.findByStableIdentifiers(Arrays.<String>asList(stId, stId2), RelationshipDirection.OUTGOING);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, databaseObjectObserved.size());
        logger.info("Finished");
    }

    // Undirected will load all relationships todo: modify test
    @Test
    public void findByDbIdWithRelationshipDirectionAndRelationshipsTest() {
        logger.info("Started testing genericService.findByDbIdWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Pathway databaseObjectObserved = (Pathway) generalService.findByDbId(dbId, RelationshipDirection.OUTGOING, "hasEvent");
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(8, databaseObjectObserved.getHasEvent().size());
        logger.info("Finished");
    }

    // Undirected will load all relationships todo: modify test
    @Test
    public void findByStableIdentifierWithRelationshipDirectionAndRelationshipsTest() {
        logger.info("Started testing genericService.findByStableIdentifierWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Pathway databaseObjectObserved = (Pathway) generalService.findByStableIdentifier(stId, RelationshipDirection.OUTGOING, "hasEvent");
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(8, databaseObjectObserved.getHasEvent().size());
        logger.info("Finished");
    }

    @Test
    public void findByDbIdsWithRelationshipDirectionAndRelationshipsTest() {
        logger.info("Started testing genericService.findByDbIdsWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Collection<DatabaseObject> databaseObjectObserved = generalService.findByDbIds(Arrays.asList(dbId, dbId2), RelationshipDirection.OUTGOING, "hasEvent", "referenceEntity");
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, databaseObjectObserved.size());
        logger.info("Finished");
    }

    @Test
    public void findByStableIdentifiersWithRelationshipDirectionAndRelationshipsTest() {
        logger.info("Started testing genericService.findByDbIdsWithRelationshipDirectionAndRelationships");
        long start, time;
        start = System.currentTimeMillis();
        Collection<DatabaseObject> databaseObjectObserved = generalService.findByStableIdentifiers(Arrays.asList(stId, stId2), RelationshipDirection.OUTGOING, "hasEvent", "referenceEntity");
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(2, databaseObjectObserved.size());
        logger.info("Finished");
    }

    /**
     * This method can hardly be tested. GkInstance does not provide any comparison since pagination is not possible.
     */
    @Test
    public void getObjectsByClassNameTest() throws ClassNotFoundException {

        logger.info("Started testing genericService.getObjectsByClassName");
        long start, time;
        start = System.currentTimeMillis();
        Collection<Pathway> pathways = generalService.findObjectsByClassName("Pathway", 1, 25);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(25, pathways.size());
        logger.info("Finished");
    }

    @Test
    public void saveAndDeleteTest() {
        Pathway pathway = new Pathway();
        pathway.setDbId(111111111111L);
        pathway.setStableIdentifier("R-HSA-111111111111");
        pathway.setDisplayName("TestPathway");

        long count = generalService.countEntries(Pathway.class);
        generalService.save(pathway);
        long countAfterSave = generalService.countEntries(Pathway.class);
        assertEquals(count + 1, countAfterSave);

        generalService.delete(111111111111l);

        long countAfterDelete = generalService.countEntries(Pathway.class);
        assertEquals(count, countAfterDelete);
    }

    @Test
    public void saveAndDeleteWithDepthTest() {
        Pathway pathway = new Pathway();
        pathway.setDbId(111111111111L);
        pathway.setStableIdentifier("R-HSA-111111111111");
        pathway.setDisplayName("TestPathway");

        Pathway pathway2 = new Pathway();
        pathway2.setDbId(111111111112L);
        pathway2.setStableIdentifier("R-HSA-111111111112");
        pathway2.setDisplayName("TestPathway2");

        Pathway pathway3 = new Pathway();
        pathway3.setDbId(111111111113L);
        pathway3.setStableIdentifier("R-HSA-111111111113");
        pathway3.setDisplayName("TestPathway3");

        List<Event> hasEvent = new ArrayList<>();
        hasEvent.add(pathway2);
        hasEvent.add(pathway3);
        pathway.setHasEvent(hasEvent);

        long count = generalService.countEntries(Pathway.class);
        generalService.save(pathway,1);
        long countAfterSave = generalService.countEntries(Pathway.class);
        assertEquals(count + 3, countAfterSave);
        // delete will delete all relationships, nethertheless the other Nodes will still be present
        generalService.delete(111111111111L);
        generalService.delete(111111111112L);
        generalService.delete(111111111113L);
        long countAfterDelete = generalService.countEntries(Pathway.class);
        assertEquals(count, countAfterDelete);
    }

    @Test
    public void countEntriesTest() {
        logger.info("Started testing databaseObjectService.countEntries");
        long start, time;
        start = System.currentTimeMillis();
        long count = generalService.countEntries(Pathway.class);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");
        assertEquals(21191l, count);
        logger.info("Finished");
    }

    /**
     * This method can hardly be tested. GkInstance does not provide any comparison and the static number will
     * possibly change when content is added to reactome. This method provides all different labels used in the
     * graph paired with the numbers of entries belonging to these labels.
     */
    @Test
    public void testGetSchemaClassCounts() {

        logger.info("Started testing databaseObjectService.getLabelsCount");
        long start, time;
        start = System.currentTimeMillis();
        Collection<SchemaClassCount> schemaClassCounts = generalService.getSchemaClassCounts();
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(59, schemaClassCounts.size());
        logger.info("Finished");
    }

    @Test
    public void testGetAllSpecies() {

        logger.info("Started testing genericService.getSpecies");
        long start, time;
        start = System.currentTimeMillis();
        Set<Species> observedSpecies = new HashSet<>(generalService.getAllSpecies());
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        Set<Species> expectedSpecies = new HashSet<>(DatabaseObjectFactory.getSpecies());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(expectedSpecies, observedSpecies);
        logger.info("Finished");
    }

    @Test
    public void testGetAllChemicals() {

        logger.info("Started testing genericService.getSpecies");
        long start, time;
        start = System.currentTimeMillis();
        Set<ReferenceEntity> observedChemicals = new HashSet<>(generalService.getAllChemicals());
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(1662, observedChemicals.size());
        logger.info("Finished");
    }

    @Test
    public void testGetTopLevelPathways() {
        logger.info("Started testing genericService.getTopLevelPathways");
        long start, time;
        start = System.currentTimeMillis();
        Collection<TopLevelPathway> observedTlps = generalService.getTopLevelPathways();
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        Collection<DatabaseObject> expectedTlps = DatabaseObjectFactory.loadFrontPageItems();
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(expectedTlps.size(), observedTlps.size());
        logger.info("Finished");
    }

    @Test
    public void getTopLevelPathwaysWithId() {
        logger.info("Started testing genericService.getTopLevelPathwaysWithId");
        long start, time;
        start = System.currentTimeMillis();
        Collection<TopLevelPathway> observedTlps = generalService.getTopLevelPathways(48887L);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(24,observedTlps.size());
        logger.info("Finished");
    }

    @Test
    public void getTopLevelPathwaysWithName() {
        logger.info("Started testing genericService.getTopLevelPathwaysWithName");
        long start, time;
        start = System.currentTimeMillis();
        Collection<TopLevelPathway> observedTlps = generalService.getTopLevelPathways("Homo sapiens");
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertEquals(24,observedTlps.size());
        logger.info("Finished");
    }

    @Test
    public void getPathwaysForTest(){
        logger.info("Started testing genericService.getPathwaysForTest");
        long start, time;
        start = System.currentTimeMillis();
        Collection<SimpleDatabaseObject> pathways = generalService.getPathwaysFor("R-ALL-113592", 48887L);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        assertTrue("There should be 429 or more pathways with ATP (R-ALL-113592)", pathways.size() >= 429);
        logger.info("Finished");
    }
}
