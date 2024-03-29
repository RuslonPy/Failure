package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import jpa.experiment.experimentjpa.model.ListenerDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAltcraftRequest {
    private String token;
    @JsonProperty("db_id")
    private Integer dbId;
    private String matching = "phone";
    @JsonProperty("detect_geo")
    private Boolean detectGeo = true;
    private ListenerDto data;

}
