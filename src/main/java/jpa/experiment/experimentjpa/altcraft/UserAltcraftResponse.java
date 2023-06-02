package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAltcraftResponse {
    @JsonProperty("error")
    private Integer error;
    @JsonProperty("error_text")
    private String error_text;
    @JsonProperty("profile_id")
    private String profile_id;
}
