package com.academic.calendar;

import com.academic.calendar.dao.ConferenceDao;
import com.academic.calendar.dao.elasticsearch.ConferenceRepository;
import com.academic.calendar.entity.ConferenceES;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AcadCalMapApplication.class)
public class ElasticsearchTests {

    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ConferenceDao conferenceDao;

    @Test
    public void testInsert() {
//        conferenceRepository.save(conferenceDao.selectConferenceById(1));
        for (int i = 2; i < 3121; i++) {
//            ConferenceES conference = conferenceDao.selectConferenceById(i);
//            if (conference != null) {
//                conferenceRepository.save(conference);
//            }
        }
    }

    @Test
    public void testSearchByTemplate() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("Co",
                        "conference", "categories", "location"))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("conference").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("categories").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("location").preTags("<em>").postTags("</em>")
                ).build();
        Page<ConferenceES> page = elasticsearchTemplate.queryForPage(searchQuery, ConferenceES.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                if (hits.getTotalHits() <= 0) {
                    return null;
                }
                List<ConferenceES> list = new ArrayList<>();
                for (SearchHit hit : hits) {
                    ConferenceES conference = new ConferenceES();

                    String id = hit.getSourceAsMap().get("id").toString();
                    conference.setId(Integer.parseInt(id));

                    String conf = hit.getSourceAsMap().get("conference").toString();
                    conference.setConference(conf);

                    String link = hit.getSourceAsMap().get("link").toString();
                    conference.setLink(link);

                    String categories = hit.getSourceAsMap().get("categories").toString();
                    conference.setCategories(categories);

                    String startTime = hit.getSourceAsMap().get("startTime").toString();
                    conference.setStartTime(startTime);

                    String endTime = hit.getSourceAsMap().get("endTime").toString();
                    conference.setEndTime(endTime);

                    String location = hit.getSourceAsMap().get("location").toString();
                    conference.setLocation(location);

                    String submissionDeadline = hit.getSourceAsMap().get("submissionDeadline").toString();
                    conference.setSubmissionDeadline(submissionDeadline);

                    //高亮显示处理
                    HighlightField conferenceField = hit.getHighlightFields().get("conference");
                    if (conferenceField != null) {
                        conference.setConference(conferenceField.getFragments()[0].toString());
                    }
                    HighlightField categoriesField = hit.getHighlightFields().get("categories");
                    if (categoriesField != null) {
                        conference.setCategories(categoriesField.getFragments()[0].toString());
                    }
                    HighlightField locationField = hit.getHighlightFields().get("location");
                    if (locationField != null) {
                        conference.setLocation(locationField.getFragments()[0].toString());
                    }
                    list.add(conference);
                }
                // 返回格式 ！  注意参数
                return new AggregatedPageImpl(list, pageable,
                        hits.getTotalHits(), searchResponse.getAggregations(), searchResponse.getScrollId(), hits.getMaxScore());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        //当前第几页
        System.out.println(page.getNumber());
        //每一页几条
        System.out.println(page.getSize());
        for (ConferenceES c : page) {
            System.out.println(c);
        }
    }
}
