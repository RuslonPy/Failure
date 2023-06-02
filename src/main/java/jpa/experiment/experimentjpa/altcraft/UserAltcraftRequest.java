package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpa.experiment.experimentjpa.model.ListenerDto;
import lombok.Data;

@Data
public class UserAltcraftRequest {
    @JsonProperty("db_id")
    private Integer dbId;
    private String token;
    private ListenerDto data;
    private String matching = "phone";
    @JsonProperty("field_name")
    private String fieldName = "phone";
    @JsonProperty("detect_geo")
    private Boolean detectGeo = true;

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ListenerDto to JSON", e);
        }
    }
}
