package com.example.ruleengine.dto;

import java.util.Map;

/**
 * DTO for rule evaluation requests.
 * Contains runtime data that will be used to evaluate rule conditions.
 */
public class RuleEvaluationRequest {

    /**
     * Runtime data as key-value pairs that will be available in the MVEL expression
     */
    private Map<String, Object> runtimeData;

    public RuleEvaluationRequest() {
    }

    public RuleEvaluationRequest(Map<String, Object> runtimeData) {
        this.runtimeData = runtimeData;
    }

    public Map<String, Object> getRuntimeData() {
        return runtimeData;
    }

    public void setRuntimeData(Map<String, Object> runtimeData) {
        this.runtimeData = runtimeData;
    }

    @Override
    public String toString() {
        return "RuleEvaluationRequest{" +
                "runtimeData=" + runtimeData +
                '}';
    }
} 