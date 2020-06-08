package com.academic.calendar;

import com.academic.calendar.entity.ConferenceES;
import com.academic.calendar.service.CalendarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AcadCalMapApplication.class)
public class CalendarTests {
    @Autowired
    private CalendarService calendarService;

    @Test
    public void testCalendar() throws IOException {
        ConferenceES c = new ConferenceES();
        c.setId(1231);
        c.setConference("HITdasxWxH  ccx 2020:xxxxxxxxxxxxxsacxxxxxxxxxxxx");
        c.setStartTime("2020-6-29");
        c.setEndTime("2020-06-30");
        c.setLocation("suzhou,China");
//        calendarService.grabEvents(c);
    }
}
