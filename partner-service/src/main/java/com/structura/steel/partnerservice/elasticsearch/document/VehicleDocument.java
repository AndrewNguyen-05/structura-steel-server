package com.structura.steel.partnerservice.elasticsearch.document;

import com.structura.steel.commons.enumeration.VehicleType;
import jakarta.persistence.Column;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "vehicles")
@Setting(settingPath = "elasticsearch/settings.json")
public class VehicleDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private VehicleType vehicleType;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String vehicleCode;

    @Field(type = FieldType.Long)
    private Long partnerId;

    @Field(type = FieldType.Keyword)
    private String licensePlate;

    private Double capacity;

    @Field(type = FieldType.Keyword)
    private String description;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String driverName;

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

    @Field(type = FieldType.Search_As_You_Type)
    private String suggestion;
}
