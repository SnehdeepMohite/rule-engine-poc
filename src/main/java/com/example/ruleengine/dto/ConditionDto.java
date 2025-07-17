package com.example.ruleengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionDto {
    private String operator; // AND, OR
    private List<SingleCondition> conditions;

    // Getters
    public String getOperator() { return operator; }
    public List<SingleCondition> getConditions() { return conditions; }

    // Setters
    public void setOperator(String operator) { this.operator = operator; }
    public void setConditions(List<SingleCondition> conditions) { this.conditions = conditions; }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleCondition {
        private String field;
        private String operator; // equals, not_equals, greater_than, less_than, in, not_in, contains
        private Object value;

        // Getters
        public String getField() { return field; }
        public String getOperator() { return operator; }
        public Object getValue() { return value; }

        // Setters
        public void setField(String field) { this.field = field; }
        public void setOperator(String operator) { this.operator = operator; }
        public void setValue(Object value) { this.value = value; }
    }
} 