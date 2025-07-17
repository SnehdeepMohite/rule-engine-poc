package com.example.ruleengine.controller;

import com.example.ruleengine.dto.RuleEvaluationRequest;
import com.example.ruleengine.dto.RuleEvaluationResponse;
import com.example.ruleengine.service.RuleEvaluatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for testing rule evaluation functionality.
 * Provides endpoints to test MVEL expression evaluation with runtime data.
 */
@RestController
@RequestMapping("/rules")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:3000"})
public class RuleTestController {

    private static final Logger log = LoggerFactory.getLogger(RuleTestController.class);

    /**
     * Hardcoded condition for testing purposes
     */
    private static final String TEST_CONDITION = "order.amount > 1000 && order.country == 'IN'";

    private final RuleEvaluatorService ruleEvaluatorService;

    public RuleTestController(RuleEvaluatorService ruleEvaluatorService) {
        this.ruleEvaluatorService = ruleEvaluatorService;
    }

    /**
     * Evaluates a hardcoded rule condition with provided runtime data.
     * 
     * @param request The evaluation request containing runtime data
     * @return ResponseEntity containing the evaluation result
     */
    @PostMapping("/evaluate")
    public ResponseEntity<RuleEvaluationResponse> evaluateRule(@RequestBody RuleEvaluationRequest request) {
        log.info("Received rule evaluation request: {}", request);

        try {
            // Validate request
            if (request == null || request.getRuntimeData() == null) {
                return ResponseEntity.badRequest()
                    .body(new RuleEvaluationResponse(TEST_CONDITION, null, "Request or runtime data cannot be null"));
            }

            // Check if expression is cached
            boolean fromCache = ruleEvaluatorService.isExpressionCached(TEST_CONDITION);

            // Evaluate the condition
            boolean result = ruleEvaluatorService.evaluateCondition(TEST_CONDITION, request.getRuntimeData());

            // Create response
            RuleEvaluationResponse response = new RuleEvaluationResponse(
                TEST_CONDITION, 
                request.getRuntimeData(), 
                result, 
                fromCache
            );

            log.info("Rule evaluation completed successfully. Result: {}, From cache: {}", result, fromCache);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument in rule evaluation: {}", e.getMessage());
            RuleEvaluationResponse errorResponse = new RuleEvaluationResponse(
                TEST_CONDITION, 
                request.getRuntimeData(), 
                e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            log.error("Unexpected error during rule evaluation: {}", e.getMessage(), e);
            RuleEvaluationResponse errorResponse = new RuleEvaluationResponse(
                TEST_CONDITION, 
                request.getRuntimeData(), 
                "Internal server error: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Evaluates a custom rule condition with provided runtime data.
     * 
     * @param condition The MVEL expression to evaluate
     * @param request The evaluation request containing runtime data
     * @return ResponseEntity containing the evaluation result
     */
    @PostMapping("/evaluate/custom")
    public ResponseEntity<RuleEvaluationResponse> evaluateCustomRule(
            @RequestParam String condition,
            @RequestBody RuleEvaluationRequest request) {
        
        log.info("Received custom rule evaluation request. Condition: {}, Data: {}", condition, request);

        try {
            // Validate request
            if (request == null || request.getRuntimeData() == null) {
                return ResponseEntity.badRequest()
                    .body(new RuleEvaluationResponse(condition, null, "Request or runtime data cannot be null"));
            }

            if (condition == null || condition.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new RuleEvaluationResponse(condition, request.getRuntimeData(), "Condition cannot be null or empty"));
            }

            // Check if expression is cached
            boolean fromCache = ruleEvaluatorService.isExpressionCached(condition);

            // Evaluate the condition
            boolean result = ruleEvaluatorService.evaluateCondition(condition, request.getRuntimeData());

            // Create response
            RuleEvaluationResponse response = new RuleEvaluationResponse(
                condition, 
                request.getRuntimeData(), 
                result, 
                fromCache
            );

            log.info("Custom rule evaluation completed successfully. Result: {}, From cache: {}", result, fromCache);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument in custom rule evaluation: {}", e.getMessage());
            RuleEvaluationResponse errorResponse = new RuleEvaluationResponse(
                condition, 
                request.getRuntimeData(), 
                e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            log.error("Unexpected error during custom rule evaluation: {}", e.getMessage(), e);
            RuleEvaluationResponse errorResponse = new RuleEvaluationResponse(
                condition, 
                request.getRuntimeData(), 
                "Internal server error: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Gets cache statistics for the rule evaluator service.
     * 
     * @return ResponseEntity containing cache statistics
     */
    @GetMapping("/evaluate/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        log.info("Retrieving cache statistics");

        Map<String, Object> stats = Map.of(
            "cacheSize", ruleEvaluatorService.getCacheSize(),
            "testConditionCached", ruleEvaluatorService.isExpressionCached(TEST_CONDITION)
        );

        return ResponseEntity.ok(stats);
    }

    /**
     * Clears the expression cache.
     * 
     * @return ResponseEntity confirming cache clearance
     */
    @DeleteMapping("/evaluate/cache")
    public ResponseEntity<Map<String, String>> clearCache() {
        log.info("Clearing expression cache");
        
        ruleEvaluatorService.clearCache();
        
        return ResponseEntity.ok(Map.of("message", "Expression cache cleared successfully"));
    }

    /**
     * Health check endpoint for the rule evaluator service.
     * 
     * @return ResponseEntity with service status
     */
    @GetMapping("/evaluate/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        log.debug("Health check requested for rule evaluator service");
        
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "RuleEvaluatorService",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
} 