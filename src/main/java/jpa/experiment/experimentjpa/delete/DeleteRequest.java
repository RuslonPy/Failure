package jpa.experiment.experimentjpa.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import jpa.experiment.experimentjpa.model.ListenerDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequest {
    @JsonProperty("db_id")
    private Integer dbId = 1;
    private String token;
    private String matching = "phone";
    private String phone;
}
