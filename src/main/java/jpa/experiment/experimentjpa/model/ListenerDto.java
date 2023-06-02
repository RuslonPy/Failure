package jpa.experiment.experimentjpa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class ListenerDto {
    @JsonProperty("user_ID")
    private Long id;
    @JsonProperty("phones")
    private List<String> phones = new ArrayList<>();
    @JsonProperty("lang")
    private Lang lang;
    @JsonProperty("appVersion")
    private String appVersion;
    @JsonProperty("identification")
    private List<String> identification = new ArrayList<>();
    @JsonProperty("_regdate")
    private Long registeredDate;
    @JsonProperty("_city")
    private String city;
    @JsonProperty("_bdate")
    private Timestamp birthday;
}
