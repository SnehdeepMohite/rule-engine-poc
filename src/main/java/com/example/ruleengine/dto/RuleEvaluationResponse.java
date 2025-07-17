package com.example.ruleengine.dto;

import java.time.LocalDateTime;

/**
 * DTO for rule evaluation responses.
 * Contains the evaluation result and metadata about the evaluation.
 */
public class RuleEvaluationResponse {

    /**
     * The condition that was evaluated
     */
    private String condition;

    /**
     * The runtime data used for evaluation
     */
    private Object runtimeData;

    /**
     * The boolean result of the evaluation
     */
    private boolean result;

    /**
     * Timestamp when the evaluation was performed
     */
    private LocalDateTime evaluatedAt;

    /**
     * Whether the expression was served from cache
     */
    private boolean fromCache;

    /**
     * Error message if evaluation failed (null if successful)
     */
    private String errorMessage;

    public RuleEvaluationResponse() {
        this.evaluatedAt = LocalDateTime.now();
    }

    public RuleEvaluationResponse(String condition, Object runtimeData, boolean result, boolean fromCache) {
        this.condition = condition;
        this.runtimeData = runtimeData;
        this.result = result;
        this.fromCache = fromCache;
        this.evaluatedAt = LocalDateTime.now();
    }

    public RuleEvaluationResponse(String condition, Object runtimeData, String errorMessage) {
        this.condition = condition;
        this.runtimeData = runtimeData;
        this.errorMessage = errorMessage;
        this.evaluatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Object getRuntimeData() {
        return runtimeData;
    }

    public void setRuntimeData(Object runtimeData) {
        this.runtimeData = runtimeData;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "RuleEvaluationResponse{" +
                "condition='" + condition + '\'' +
                ", runtimeData=" + runtimeData +
                ", result=" + result +
                ", evaluatedAt=" + evaluatedAt +
                ", fromCache=" + fromCache +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
} 