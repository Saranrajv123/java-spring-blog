package com.example.blogpractice.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(
        value = { "createdBy", "updatedBy" },
        allowGetters = true
)
public abstract class UserDateAudit extends DateAudit {

    @JsonIgnore
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @JsonIgnore
    @LastModifiedBy
    private String updatedBy;
}
