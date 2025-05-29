package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PartnerProjectSearchRepository extends ElasticsearchRepository<PartnerProjectDocument,Long> {

    Page<PartnerProjectDocument> getAllByPartnerIdAndDeleted(Long partnerId, boolean deleted, Pageable p);

    @Query("""
      {
        "bool": {
          "must": [
            { "multi_match": {
                "query": "?0",
                "type": "phrase_prefix",
                "analyzer": "folding",
                "fields":[
                    "projectName",
                    "projectName._2gram",
                    "projectCode",
                    "projectCode._2gram"
                ]
            }},
            { "term": { "partnerId": ?1 }},
            { "term": { "deleted":   ?2 }}
          ]
        }
      }
    """)
    Page<PartnerProjectDocument> searchByKeywordAndPartnerId(String kw, Long partnerId, boolean deleted, Pageable p);

    @Query("""
      {
        "bool": {
          "must":[
            { "multi_match": {
                "query":"?0",
                "type":"bool_prefix",
                "analyzer":"folding",
                "fields":[ "suggestion","suggestion._2gram","suggestion._3gram" ]
            }},
            { "term": { "partnerId": ?2 }},
            { "term": { "deleted": ?1 }}
          ]
        }
      }
    """)
    Page<PartnerProjectDocument> findBySuggestionPrefix(String prefix, boolean deleted, Long partnerId, Pageable p);
}