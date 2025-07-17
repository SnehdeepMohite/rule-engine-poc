package com.example.ruleengine.service;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for evaluating rule conditions using MVEL expression language.
 * Provides caching of compiled expressions for improved performance.
 */
@Service
public class RuleEvaluatorService {

    private static final Logger log = LoggerFactory.getLogger(RuleEvaluatorService.class);

    /**
     * Cache for compiled MVEL expressions to avoid re-compilation
     */
    private final Map<String, Serializable> expressionCache = new ConcurrentHashMap<>();

    /**
     * Parser context for MVEL compilation
     */
    private final ParserContext parserContext;

    public RuleEvaluatorService() {
        this.parserContext = new ParserContext();
        // Configure parser context for better security and performance
        parserContext.setStrictTypeEnforcement(true);
        parserContext.setStrongTyping(true);
    }

    /**
     * Evaluates a rule condition using MVEL expression language.
     * 
     * @param condition The MVEL expression as a string (e.g., "order.amount > 1000 && order.country == 'IN'")
     * @param runtimeData The runtime data as a Map containing variables accessible in the expression
     * @return boolean result of the expression evaluation
     * @throws IllegalArgumentException if the expression is invalid or doesn't return a boolean
     * @throws RuntimeException if there's an error during evaluation
     */
    public boolean evaluateCondition(String condition, Map<String, Object> runtimeData) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be null or empty");
        }

        if (runtimeData == null) {
            throw new IllegalArgumentException("Runtime data cannot be null");
        }

        try {
            log.debug("Evaluating condition: {} with data: {}", condition, runtimeData);

            // Get or compile the expression
            Serializable compiledExpression = getOrCompileExpression(condition);

            // Evaluate the expression
            Object result = MVEL.executeExpression(compiledExpression, runtimeData);

            // Validate that the result is a boolean
            if (result instanceof Boolean) {
                boolean booleanResult = (Boolean) result;
                log.debug("Condition evaluation result: {}", booleanResult);
                return booleanResult;
            } else {
                String errorMessage = String.format(
                    "Expression '%s' returned non-boolean result: %s (type: %s). Expected boolean.",
                    condition, result, result != null ? result.getClass().getSimpleName() : "null"
                );
                log.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

        } catch (Exception e) {
            String errorMessage = String.format(
                "Error evaluating condition '%s' with data %s: %s",
                condition, runtimeData, e.getMessage()
            );
            log.error(errorMessage, e);
            
            if (e instanceof IllegalArgumentException) {
                throw e; // Re-throw IllegalArgumentException as-is
            } else {
                throw new RuntimeException(errorMessage, e);
            }
        }
    }

    /**
     * Gets a compiled expression from cache or compiles it if not cached.
     * 
     * @param condition The MVEL expression to compile
     * @return Compiled expression as Serializable
     * @throws IllegalArgumentException if the expression cannot be compiled
     */
    private Serializable getOrCompileExpression(String condition) {
        return expressionCache.computeIfAbsent(condition, this::compileExpression);
    }

    /**
     * Compiles an MVEL expression.
     * 
     * @param condition The MVEL expression to compile
     * @return Compiled expression as Serializable
     * @throws IllegalArgumentException if the expression cannot be compiled
     */
    private Serializable compileExpression(String condition) {
        try {
            log.debug("Compiling MVEL expression: {}", condition);
            Serializable compiled = MVEL.compileExpression(condition, parserContext);
            log.debug("Successfully compiled expression: {}", condition);
            return compiled;
        } catch (Exception e) {
            String errorMessage = String.format(
                "Failed to compile MVEL expression '%s': %s",
                condition, e.getMessage()
            );
            log.error(errorMessage, e);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    /**
     * Clears the expression cache.
     * Useful for testing or when memory usage needs to be managed.
     */
    public void clearCache() {
        int cacheSize = expressionCache.size();
        expressionCache.clear();
        log.info("Cleared expression cache. Removed {} cached expressions.", cacheSize);
    }

    /**
     * Gets the current size of the expression cache.
     * 
     * @return Number of cached expressions
     */
    public int getCacheSize() {
        return expressionCache.size();
    }

    /**
     * Checks if an expression is cached.
     * 
     * @param condition The expression to check
     * @return true if the expression is cached, false otherwise
     */
    public boolean isExpressionCached(String condition) {
        return expressionCache.containsKey(condition);
    }
} 