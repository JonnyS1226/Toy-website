package com.academic.calendar.dao.elasticsearch;

import com.academic.calendar.entity.ConferenceES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends ElasticsearchRepository<ConferenceES, Integer> {
}
