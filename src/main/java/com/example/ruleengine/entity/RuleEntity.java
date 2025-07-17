package com.example.ruleengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rules")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product", nullable = false)
    private String product;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "conditions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String conditions;

    @Column(name = "actions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String actions;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Getters
    public UUID getId() { return id; }
    public String getProduct() { return product; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Integer getPriority() { return priority; }
    public String getConditions() { return conditions; }
    public String getActions() { return actions; }
    public Boolean getEnabled() { return enabled; }
    public Integer getVersion() { return version; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setProduct(String product) { this.product = product; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    public void setActions(String actions) { this.actions = actions; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public void setVersion(Integer version) { this.version = version; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (version == null) {
            version = 1;
        }
        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 