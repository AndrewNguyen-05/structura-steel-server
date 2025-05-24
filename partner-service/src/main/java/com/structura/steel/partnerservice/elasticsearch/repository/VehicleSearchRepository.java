package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VehicleSearchRepository extends ElasticsearchRepository<VehicleDocument, Long> {

    Page<VehicleDocument> getAllByPartnerId(Long partnerId, Pageable pageable);

    Page<VehicleDocument> getAllByPartnerIdAndDeleted(Long partnerId, boolean deleted, Pageable page);

	@Query("""
           {
             "bool": {
               "must": [
                 {
                   "multi_match": {
                     "query": "?0",
                     "type": "phrase_prefix",
                     "analyzer": "folding",
                     "fields": [
                       "driverName", "driverName._2gram",
                       "vehicleCode", "vehicleCode._2gram",
                       "licensePlate"
                     ]
                   }
                 },
                 { "term": { "partnerId": ?1 } },
                 { "term": { "deleted": ?2 } }
               ]
             }
           }
           """)
    Page<VehicleDocument> searchByKeywordAndPartnerId(String searchKeyword, Long partnerId, boolean deleted, Pageable pageable);

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
			  { "term": { "deleted": ?1 } },
			  { "term": { "partnerId": ?2 } }
			]
		  }
		}
		""")
    Page<VehicleDocument> findBySuggestionPrefix(String prefixKeyword, boolean deleted, Long partnerId, Pageable pageable);
    // "?0" sẽ lấy giá trị của tham số đầu tiên (prefixKeyword)
}
