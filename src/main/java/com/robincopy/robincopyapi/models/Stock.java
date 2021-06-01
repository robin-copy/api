package com.robincopy.robincopyapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
@Builder
public class Stock extends AbstractEntity {

    private String symbol;

    private String companyName;

}
