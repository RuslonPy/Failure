package jpa.experiment.experimentjpa.failure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failed_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "")
    private Long errUserId;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private LocalDateTime failOccuredTime;

}
