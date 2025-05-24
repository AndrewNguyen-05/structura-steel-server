package com.structura.steel.coreservice.elasticsearch.repository;

import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SaleOrderSearchRepository extends ElasticsearchRepository<SaleOrderDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query": "?0",
              "type": "bool_prefix",
              "analyzer": "folding",
              "fields": [
                "exportCode",
                "exportCode._2gram",
              ]
            }
          }
          """)
    Page<SaleOrderDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("""
          {
            "multi_match": {
              "query": "?0",
              "type": "bool_prefix",
              "analyzer": "folding",
              "fields": [
                "suggestion",
                "suggestion._2gram",
                "suggestion._3gram"
              ]
            }
          }
          """)
    Page<SaleOrderDocument> findBySuggestionPrefix(String prefix, Pageable pageable);
}
