package jpa.experiment.experimentjpa.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ListenerEntityDto {

    private Long id;
    private String phone;
    private String customerId;
    private Long registeredDate;
    private Timestamp birthday;
    private String appVersion;
    private String apelsinCustomerId;
    private String city;
    private Lang lang;
    private String man;
    private String san;
    private String anavi;
    private String inabi;
    private String biza;
    private String siza;
}
