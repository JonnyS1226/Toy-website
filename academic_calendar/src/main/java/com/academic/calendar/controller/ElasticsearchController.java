package com.academic.calendar.controller;

import com.academic.calendar.entity.ConferenceES;
import com.academic.calendar.entity.Page;
import com.academic.calendar.entity.User;
import com.academic.calendar.service.ConferenceService;
import com.academic.calendar.service.ElasticsearchService;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * elasticsearch全文检索控制器
 */
@Controller
public class ElasticsearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserHolder userHolder;

    // get请求带参数，全文检索
    @RequestMapping(path = "/es/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        org.springframework.data.domain.Page<ConferenceES> searchResult =
                elasticsearchService.searchConference(keyword, page.getCurrent()-1, page.getLimit());
        List<ConferenceES> conferences = new ArrayList<>();
        if (searchResult != null) {
            for (ConferenceES conference : searchResult) {
                conferences.add(conference);
            }
        }
        model.addAttribute("conferences", conferences);
        model.addAttribute("keyword", keyword);
        // 分页
        page.setPath("/es/search?keyword=" + keyword);
        page.setRows(searchResult == null?0: (int) searchResult.getTotalElements());
        return "search";
    }
}
