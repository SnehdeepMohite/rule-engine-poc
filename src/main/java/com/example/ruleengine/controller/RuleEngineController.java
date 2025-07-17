package com.example.ruleengine.controller;

import com.example.ruleengine.dto.ActionDto;
import com.example.ruleengine.dto.EvaluateRequest;
import com.example.ruleengine.engine.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluate")
public class RuleEngineController {

    private static final Logger log = LoggerFactory.getLogger(RuleEngineController.class);

    private RuleEngine ruleEngine;

    // Constructor for dependency injection
    public RuleEngineController(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> evaluateRules(@RequestBody EvaluateRequest request) {
        log.info("Evaluating rules for product: {}", request.getProduct());
        
        try {
            List<ActionDto.SingleAction> matchedActions = ruleEngine.evaluate(
                request.getProduct(), 
                request.getPayload()
            );
            
            Map<String, Object> response = Map.of(
                "product", request.getProduct(),
                "matchedActions", matchedActions,
                "actionCount", matchedActions.size()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error evaluating rules: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to evaluate rules: " + e.getMessage()));
        }
    }

    @GetMapping("/cache/status")
    public ResponseEntity<Map<String, Object>> getCacheStatus() {
        log.info("Getting cache status");
        
        try {
            Map<String, List<Object>> cacheStatus = (Map<String, List<Object>>) (Map<?, ?>) ruleEngine.getCacheStatus();
            
            Map<String, Object> response = Map.of(
                "cacheStatus", cacheStatus,
                "totalProducts", cacheStatus.size(),
                "totalRules", cacheStatus.values().stream()
                    .mapToInt(List::size)
                    .sum()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error getting cache status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to get cache status: " + e.getMessage()));
        }
    }
} 