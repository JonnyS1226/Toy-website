package com.academic.calendar.service;

import com.academic.calendar.dao.ConferenceDao;
import com.academic.calendar.entity.Conference;
import com.academic.calendar.util.GoogleCalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

/**
 * 在本地创建ics 业务            by jfj
 * 导入谷歌日历  业务            by sjy
 */
@Service
public class CalendarService {

    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private GoogleCalendarUtil googleCalendarUtil;

    // 生成ics文件(D://)
    //edit by jfj
    //主要流程
    public void grabEvents(Conference conference) throws IOException {
        try {
            //content是文件头的内容
            String content = "BEGIN:VCALENDAR\n" +
                    "PRODID:-//Google Inc//Google Calendar 70.9054//EN\n" +
                    "VERSION:2.0\n" +
                    "CALSCALE:GREGORIAN\n" +
                    "METHOD:PUBLISH\n" +
                    "X-WR-CALNAME:995740977@qq.com\n"+
                    "X-WR-TIMEZONE:Asia/Shanghai\n";

            //目录生成在c盘根目录
            File file = new File("D:\\" + conference.getConference().substring(0, 10) + ".ics");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(content);

            bw.write("BEGIN:VEVENT\n");
            String start_time=time_init(conference.getStartTime());//生成开始时间
            String end_time=time_init(conference.getEndTime());//生成结束时间
            //logger.debug(end_time);
            String UTCTimeStr = getUTCTimeStr() ;

            String dstart="DTSTART:"+start_time+"T140000Z"+"\n";
            String dtend="DTEND:"+end_time+"T140000Z"+"\n";
            String stamp="DTSTAMP:"+getUTCTimeStr()+"\n";
            String uid="UID:"+getUID(conference.getId());//这里的UID不能相同，要不然无法导入
            String created="CREATED:"+getUTCTimeStr()+"\n";
            String describtion="DESCRIPTION:" + conference.getConference()+"\n";
            String lastmodified="LAST-MODIFIED:" + getUTCTimeStr()+"\n";
            String c="LOCATION:" + conference.getLocation()+"\n";
            String sequence="SEQUENCE:1\n";
            String status="STATUS:CONFIRMED"+"\n";
            String summary="SUMMARY:"+ conference.getConference().substring(0, 25) + "\n";
            String transp="TRANSP:OPAQUE"+"\n";
            bw.write(dstart+dtend+stamp+uid+created+describtion+lastmodified+c+sequence+status+summary+transp);
            bw.write("END:VEVENT\n");
            bw.write("END:VCALENDAR\n");

            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    //字符串的处理
    public String time_init(String dtime){
        String[] sArray=dtime.split("-");
        String year=sArray[0];
        String month=sArray[1];
        String day=sArray[2];
        return year+month+day;

    }

    //计算UTC时间
    public static String getUTCTimeStr() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        UTCTimeBuffer.append(year);
        if(month<10){
            UTCTimeBuffer.append("0");  //如果月份小于10，则补0
        }
        UTCTimeBuffer.append(month);
        if(day<10){
            UTCTimeBuffer.append("0");//如果日期小于10，则补0
        }
        UTCTimeBuffer.append(day).append("T");

        if(hour<10){
            UTCTimeBuffer.append("0");
        }
        UTCTimeBuffer.append(hour);
        if(minute<10){
            UTCTimeBuffer.append("0");
        }
        UTCTimeBuffer.append(minute).append("00").append("Z");

        //format.parse(UTCTimeBuffer.toString()) ;
        return UTCTimeBuffer.toString() ;

    }

    //用哈希为每个事件生成UID
    public static String getUID(int i){
        String str = DigestUtils.md5DigestAsHex(String.valueOf(i).getBytes());
        return str+"\n";
    }

    // 添加到谷歌日历业务
    public void  addEventToGoogleCalendar(Conference conference) {
        try {
            googleCalendarUtil.addEventToCalendar(conference);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
