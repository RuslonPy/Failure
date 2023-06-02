package jpa.experiment.experimentjpa.altcraft;

import lombok.Data;

import java.util.List;

@Data
public class AltCraftResponseImport {
    private Integer error;
    private String error_text;
    private List<UserAltcraftResponse> result;
}
