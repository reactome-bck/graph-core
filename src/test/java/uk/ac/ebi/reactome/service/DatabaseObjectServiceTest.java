package uk.ac.ebi.reactome.service;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.reactome.config.MyConfiguration;
import uk.ac.ebi.reactome.data.DatabaseObjectFactory;
import uk.ac.ebi.reactome.domain.model.DatabaseObject;
import uk.ac.ebi.reactome.domain.model.ReferenceEntity;
import uk.ac.ebi.reactome.domain.result.LabelsCount;
import uk.ac.ebi.reactome.util.JunitHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by:
 *
 * @author Florian Korninger (florian.korninger@ebi.ac.uk)
 * @since 10.11.15.
 *
 */
@ContextConfiguration(classes = { MyConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseObjectServiceTest {

    private static final Logger logger = LoggerFactory.getLogger("testLogger");

    private static final Long dbId = 5205685L;
    private static final String stId = "R-HSA-5205685";

    @Autowired
    private DatabaseObjectService databaseObjectService;

    @BeforeClass
    public static void setUpClass () {
        logger.info("\n --- Running DatabaseObjectServiceTests --- \n");
    }

    @Before
    public void setUp() throws Exception {
        databaseObjectService.findByDbId(1l);
        DatabaseObjectFactory.createObject("1");
        DatabaseObjectFactory.clearCache();
    }

    @After
    public void tearDown() {}

    @Test
    public void testFindByDbId() throws InvocationTargetException, IllegalAccessException {

        logger.info("Started testing databaseObjectService.findByDbId");
        long start, time;

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = databaseObjectService.findByDbId(dbId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        databaseObjectExpected.load();
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertTrue(databaseObjectExpected.equals(databaseObjectObserved));
        JunitHelper.assertDatabaseObjectsEqual(databaseObjectExpected, databaseObjectObserved);
    }
    @Test
    public void testFindByDbIdNoRelations() {

        logger.info("Started testing databaseObjectService.findByDbIdNoRelations");
        long start, time;

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = databaseObjectService.findByDbIdNoRelations(dbId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(dbId.toString());
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(databaseObjectExpected.getDbId(), databaseObjectObserved.getDbId());
        assertEquals(databaseObjectExpected.getDisplayName(),databaseObjectObserved.getDisplayName());
    }

    @Test
    public void testFindByStableIdentifier() {

        logger.info("Started testing databaseObjectService.findByStableIdentifier");
        long start, time;

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectObserved = databaseObjectService.findByStableIdentifier(stId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

        start = System.currentTimeMillis();
        DatabaseObject databaseObjectExpected = DatabaseObjectFactory.createObject(stId);
        databaseObjectExpected.load();
        time = System.currentTimeMillis() - start;
        logger.info("GkInstance execution time: " + time + "ms");

        assertEquals(databaseObjectExpected.getDbId(), databaseObjectObserved.getDbId());
        assertEquals(databaseObjectExpected.getDisplayName(), databaseObjectObserved.getDisplayName());
    }

    @Test
    public void testGetParticipatingMolecules() {

        logger.info("Started testing databaseObjectService.getParticipatingMolecules");
        long start, time;

        start = System.currentTimeMillis();
        Collection<ReferenceEntity> participants = databaseObjectService.getParticipatingMolecules(dbId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

//        TODO add actual tests

    }

    @Test
    public void testGetParticipatingMolecules2() {

        logger.info("Started testing databaseObjectService.getParticipatingMolecules");
        long start, time;

        start = System.currentTimeMillis();
//        Collection<Participant> participants = databaseObjectService.getParticipatingMolecules2(dbId);
        time = System.currentTimeMillis() - start;
        logger.info("GraphDb execution time: " + time + "ms");

//        TODO add actual tests

    }

    @Test
    public void testGetLabelsCount() {
        Collection<LabelsCount> l = databaseObjectService.getLabelsCount();
        Map<String,Integer> map = new HashMap<>();
        for (LabelsCount labelsCount : l) {
            for (String s : labelsCount.getLabels()) {
                if(map.containsKey(s)) {
                    int i = map.get(s);
                    i += labelsCount.getCount();

                }
            }

        }
        System.out.println("");
    }

}