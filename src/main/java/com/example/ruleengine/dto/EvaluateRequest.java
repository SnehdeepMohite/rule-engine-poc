package com.example.ruleengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRequest {
    private String product;
    private Map<String, Object> payload;

    // Getters
    public String getProduct() { return product; }
    public Map<String, Object> getPayload() { return payload; }

    // Setters
    public void setProduct(String product) { this.product = product; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
} 