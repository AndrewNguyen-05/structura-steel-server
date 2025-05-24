package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VehicleSearchRepository extends ElasticsearchRepository<VehicleDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query": "?0",
              "type": "bool_prefix",
              "analyzer": "folding",
              "fields": [
                "driverName",
                "driverName._2gram",
                "vehicleCode",
                "vehicleCode._2gram"
              ]
            }
          }
          """)
    Page<VehicleDocument> searchByKeyword(String searchKeyword, Pageable pageable);

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
    Page<VehicleDocument> findBySuggestionPrefix(String prefix, Pageable pageable);

    Page<VehicleDocument> getAllByPartnerId(Long partnerId, Pageable pageable);
}
