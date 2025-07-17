package com.example.ruleengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionDto {
    private List<SingleAction> actions;

    // Getters
    public List<SingleAction> getActions() { return actions; }

    // Setters
    public void setActions(List<SingleAction> actions) { this.actions = actions; }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleAction {
        private String type; // discount, create_order, notification, etc.
        private Object value;
        private String description;

        // Getters
        public String getType() { return type; }
        public Object getValue() { return value; }
        public String getDescription() { return description; }

        // Setters
        public void setType(String type) { this.type = type; }
        public void setValue(Object value) { this.value = value; }
        public void setDescription(String description) { this.description = description; }
    }
} 