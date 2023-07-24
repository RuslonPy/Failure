package jpa.experiment.experimentjpa.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteResponse {

    private Data data;
    private int error;
    @JsonProperty("error_text")
    private String errorText;

    public static class Data{
        @JsonProperty("profile_id")
        private String profileId;
    }
}
