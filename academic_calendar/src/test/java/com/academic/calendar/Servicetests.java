package com.academic.calendar;

import com.academic.calendar.entity.Conference;
import com.academic.calendar.service.ConferenceService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AcadCalMapApplication.class)
public class Servicetests {
    @Autowired
    private ConferenceService conferenceService;
    Conference data = null;

    @BeforeClass
    public static void beforeClass() { }

    @AfterClass
    public static void afterClass() { }

    @Before
    public void before(){
        data = new Conference();
        data.setId(10000);
        data.setLink("test");
        data.setLocation("shanghai");
        data.setCategories("testcate");
        data.setConference("testconference");
        data.setStartTime("2020-1-1");
        data.setEndTime("2020-12-31");
        data.setSubmissionDeadline("2020-5-5");
        conferenceService.addConference(data);
    }

    @After
    public void after(){
        conferenceService.deleteConference(data.getId());
    }

    @Test
    public void testAddConference() {
        Conference data1 = new Conference();
        data1.setId(10001);
        data1.setLink("111");
        data1.setCategories("111");
        data1.setConference("111");
        Map<String, Object> map = conferenceService.addConference(data1);
        Assert.assertNull(map);
        int val = conferenceService.deleteConference(data1.getId());
        Assert.assertEquals(1, val);
    }

    @Test
    public void testFindConference() {
        Conference conferenceById = conferenceService.findConferenceById(data.getId());
        Assert.assertNotNull(conferenceById);
        Assert.assertEquals(data.getConference(), conferenceById.getConference());
        Assert.assertEquals(data.getLocation(), conferenceById.getLocation());
    }

    @Test
    public void testFindConferenceByName() {
        List<Conference> conferenceById = conferenceService.findConferenceByName(data.getConference(),0,10);
        for (Conference conference : conferenceById) {
            Assert.assertNotNull(conferenceById);
            Assert.assertEquals(data.getConference(), conference.getConference());
            Assert.assertEquals(data.getLocation(), conference.getLocation());
        }
    }

    @Test
    public void testFindConferenceByCate() {
        List<Conference> conferenceById = conferenceService.findConferenceByCate(data.getCategories(),0,10);
        for (Conference conference : conferenceById) {
            Assert.assertNotNull(conferenceById);
            Assert.assertEquals(data.getConference(), conference.getConference());
            Assert.assertEquals(data.getLocation(), conference.getLocation());
        }
    }


}
