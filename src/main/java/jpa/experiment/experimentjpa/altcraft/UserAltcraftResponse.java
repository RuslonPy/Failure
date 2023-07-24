package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserAltcraftResponse {
    @JsonProperty("error")
    private Integer error;
    @JsonProperty("error_text")
    private String errorText;
    @JsonProperty("profile_id")
    private String profileId;
}
