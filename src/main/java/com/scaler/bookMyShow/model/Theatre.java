
package com.scaler.bookMyShow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scaler.bookMyShow.model.enums.TheatreStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Theatre extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private TheatreStatus status;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonIgnore
    private City city;
}
