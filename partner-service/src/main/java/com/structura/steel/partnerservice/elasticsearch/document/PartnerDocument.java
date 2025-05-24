package com.structura.steel.partnerservice.elasticsearch.document;

import com.structura.steel.commons.enumeration.PartnerType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "partners")
@Setting(settingPath = "elasticsearch/settings.json")
public class PartnerDocument {

    @Id
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String partnerCode;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String partnerName;

    @Field(type = FieldType.Keyword)
    private PartnerType partnerType;

    @Field(type = FieldType.Keyword)
    private String taxCode;

    @Field(type = FieldType.Keyword)
    private String legalRepresentative;

    @Field(type = FieldType.Keyword)
    private String legalRepresentativePhone;

    @Field(type = FieldType.Keyword)
    private String contactPerson;

    @Field(type = FieldType.Keyword)
    private String contactPersonPhone;

    @Field(type = FieldType.Keyword)
    private String bankName;

    @Field(type = FieldType.Keyword)
    private String bankAccountNumber;

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

    @Field(type = FieldType.Search_As_You_Type)
    private String suggestion; // For partnerName suggestions
}