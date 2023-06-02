package jpa.experiment.experimentjpa.model;

import jpa.experiment.experimentjpa.config.ListenerConfig;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@EntityListeners(ListenerConfig.class)
public class ListenerEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "phone")
    private String phone;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "registered_date")
    private Long registeredDate;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "apelsin_customer_id")
    private String apelsinCustomerId;
    @Column(name = "city")
    private String city;

    @Column(name = "lang")
    @Enumerated(EnumType.STRING)
    private Lang lang;



}