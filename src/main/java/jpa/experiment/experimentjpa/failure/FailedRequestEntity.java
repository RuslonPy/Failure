package jpa.experiment.experimentjpa.failure;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failed_users")
@Data
public class FailedRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "")
    private Long errUserId;
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.FAILED;
    private LocalDateTime timestamp;

}
