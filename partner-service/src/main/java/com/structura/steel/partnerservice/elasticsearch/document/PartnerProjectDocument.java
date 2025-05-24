package com.structura.steel.partnerservice.elasticsearch.document;

import com.structura.steel.partnerservice.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "projects")
@Setting(settingPath = "elasticsearch/settings.json")
public class PartnerProjectDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long partnerId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String projectName;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String projectCode;

    @Field(type = FieldType.Keyword)
    private String projectAddress;

    @Field(type = FieldType.Keyword)
    private String projectRepresentative;

    @Field(type = FieldType.Keyword)
    private String projectRepresentativePhone;

    @Field(type = FieldType.Keyword)
    private String contactPerson;

    @Field(type = FieldType.Keyword)
    private String contactPersonPhone;

    @Field(type = FieldType.Keyword)
    private String address;

    private List<Long> productIds;

    @Field(type = FieldType.Short)
    private Short version;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Date createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Date updatedAt;

    @Field(type = FieldType.Keyword)
    private String createdBy;

    @Field(type = FieldType.Keyword)
    private String updatedBy;

    @Field(type = FieldType.Boolean)
    private Boolean deleted;

    @Field(type = FieldType.Search_As_You_Type,
            analyzer = "folding",
            searchAnalyzer = "folding")
    private String suggestion;
}
