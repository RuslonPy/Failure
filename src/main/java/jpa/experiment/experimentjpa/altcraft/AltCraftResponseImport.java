package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AltCraftResponseImport {
    private Integer error;
    @JsonProperty("error_text")
    private String errorText;
    private List<UserAltcraftResponse> result;
}
