package com.academic.calendar.util;

import com.academic.calendar.entity.Conference;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Google Calendar API接口 工具
 */
@Repository
public class GoogleCalendarUtil {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // WR
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/calendar");
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleCalendarUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void addEventToCalendar(Conference conference) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Event event = new Event();
            event.setSummary(conference.getConference().substring(0, 20));
            event.setLocation(conference.getLocation());
            event.setDescription(conference.getConference());
            if (!conference.getStartTime().equals("N/A")) {
                Date startDate = sdf.parse(conference.getStartTime());
                com.google.api.client.util.DateTime start = new com.google.api.client.util.DateTime(startDate, TimeZone.getTimeZone("UTC"));
                event.setStart(new EventDateTime().setDateTime(start));
            }
            if (!conference.getEndTime().equals("N/A")) {
                Date endDate = sdf.parse(conference.getEndTime());
                com.google.api.client.util.DateTime end = new com.google.api.client.util.DateTime(endDate, TimeZone.getTimeZone("UTC"));
                event.setEnd(new EventDateTime().setDateTime(end));
            }
            event = service.events().insert("primary", event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 测试 添加事件和查询事件，根据文档，更换权限时要删除token
     * @param args
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        GoogleCalendarUtil googleCalendarUtil = new GoogleCalendarUtil();
        Conference c = new Conference();
        c.setId(121);
        c.setConference("HITWcxxH 2020:xxxxxxxxxxxxxsacxxxxxxxxxxxx");
        c.setStartTime("2020-6-29");
        c.setEndTime("2020-06-30");
        c.setLocation("suzhou,China");
        googleCalendarUtil.addEventToCalendar(c);
    }
}
