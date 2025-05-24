package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PartnerSearchRepository extends ElasticsearchRepository<PartnerDocument, Long> {

    Page<PartnerDocument> findAllByDeleted(boolean deleted, Pageable page);

    @Query("""
          {
            "bool": {
              "must": [
                {
                  "multi_match": {
                    "query": "?0",
                    "type": "bool_prefix",
                    "analyzer": "folding",
                    "fields": [
                        "partnerName",
                        "partnerName._2gram",
                        "partnerCode",
                        "partnerCode._2gram"
                    ]
                  }
                },
                {
                	"term": { "deleted": ?1 }
                }
              ]
            }
          }
         """)
    Page<PartnerDocument> searchByKeyword(String searchKeyword,
                                          boolean deleted,
                                          Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("""
		{
		  "bool": {
			"must": [
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
			  },
			  {
				"term": { "deleted": ?1 }
			  }
			]
		  }
		}
		""")
    Page<PartnerDocument> findBySuggestionPrefix(String prefixKeyword, boolean deleted, Pageable pageable);
    // "?0" sẽ lấy giá trị của tham số đầu tiên (prefixKeyword)
}