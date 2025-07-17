package com.example.ruleengine.engine;

import com.example.ruleengine.dto.ActionDto;
import com.example.ruleengine.dto.ConditionDto;
import com.example.ruleengine.entity.RuleEntity;
import com.example.ruleengine.repository.RuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RuleEngine {

    private static final Logger log = LoggerFactory.getLogger(RuleEngine.class);

    private RuleRepository ruleRepository;
    private ObjectMapper objectMapper;

    // Cache: product -> List of rules
    private Map<String, List<RuleEntity>> ruleCache = new ConcurrentHashMap<>();

    // Explicit constructor for dependency injection
    public RuleEngine(RuleRepository ruleRepository, ObjectMapper objectMapper) {
        this.ruleRepository = ruleRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initialize() {
        refreshCache();
    }

    public void refreshCache() {
        log.info("Refreshing rule cache...");
        ruleCache.clear();
        
        List<RuleEntity> allRules = ruleRepository.findAllEnabledRulesOrderByPriority();
        
        // Group rules by product
        for (RuleEntity rule : allRules) {
            ruleCache.computeIfAbsent(rule.getProduct(), k -> new ArrayList<>()).add(rule);
        }
        
        log.info("Rule cache refreshed. Total products: {}, Total rules: {}", 
                ruleCache.size(), allRules.size());
    }

    public List<ActionDto.SingleAction> evaluate(String product, Map<String, Object> payload) {
        log.info("Evaluating rules for product: {}, payload: {}", product, payload);
        
        List<ActionDto.SingleAction> matchedActions = new ArrayList<>();
        List<RuleEntity> rules = ruleCache.get(product);
        
        if (rules == null || rules.isEmpty()) {
            log.info("No rules found for product: {}", product);
            return matchedActions;
        }

        for (RuleEntity rule : rules) {
            if (evaluateConditions(rule.getConditions(), payload)) {
                log.info("Rule matched: {}", rule.getName());
                List<ActionDto.SingleAction> actions = parseActions(rule.getActions());
                matchedActions.addAll(actions);
            }
        }

        log.info("Evaluation completed. Matched actions: {}", matchedActions.size());
        return matchedActions;
    }

    private boolean evaluateConditions(String conditionsJson, Map<String, Object> payload) {
        try {
            ConditionDto conditionDto = objectMapper.readValue(conditionsJson, ConditionDto.class);
            return evaluateConditionDto(conditionDto, payload);
        } catch (JsonProcessingException e) {
            log.error("Error parsing conditions JSON: {}", conditionsJson, e);
            return false;
        }
    }

    private boolean evaluateConditionDto(ConditionDto conditionDto, Map<String, Object> payload) {
        if (conditionDto.getConditions() == null || conditionDto.getConditions().isEmpty()) {
            return true;
        }

        boolean result = "OR".equalsIgnoreCase(conditionDto.getOperator());
        
        for (ConditionDto.SingleCondition condition : conditionDto.getConditions()) {
            boolean conditionResult = evaluateSingleCondition(condition, payload);
            
            if ("AND".equalsIgnoreCase(conditionDto.getOperator())) {
                result = result && conditionResult;
                if (!result) break; // Short circuit for AND
            } else {
                result = result || conditionResult;
                if (result) break; // Short circuit for OR
            }
        }
        
        return result;
    }

    private boolean evaluateSingleCondition(ConditionDto.SingleCondition condition, Map<String, Object> payload) {
        Object fieldValue = payload.get(condition.getField());
        Object conditionValue = condition.getValue();
        
        if (fieldValue == null) {
            return false;
        }

        switch (condition.getOperator().toLowerCase()) {
            case "equals":
                return Objects.equals(fieldValue, conditionValue);
                
            case "not_equals":
                return !Objects.equals(fieldValue, conditionValue);
                
            case "greater_than":
                return compareNumbers(fieldValue, conditionValue) > 0;
                
            case "less_than":
                return compareNumbers(fieldValue, conditionValue) < 0;
                
            case "greater_than_or_equal":
                return compareNumbers(fieldValue, conditionValue) >= 0;
                
            case "less_than_or_equal":
                return compareNumbers(fieldValue, conditionValue) <= 0;
                
            case "in":
                return evaluateInCondition(fieldValue, conditionValue);
                
            case "not_in":
                return !evaluateInCondition(fieldValue, conditionValue);
                
            case "contains":
                return fieldValue.toString().contains(conditionValue.toString());
                
            case "not_contains":
                return !fieldValue.toString().contains(conditionValue.toString());
                
            default:
                log.warn("Unknown operator: {}", condition.getOperator());
                return false;
        }
    }

    private boolean evaluateInCondition(Object fieldValue, Object conditionValue) {
        if (conditionValue instanceof Collection) {
            return ((Collection<?>) conditionValue).contains(fieldValue);
        } else if (conditionValue instanceof Object[]) {
            return Arrays.asList((Object[]) conditionValue).contains(fieldValue);
        }
        return false;
    }

    private int compareNumbers(Object fieldValue, Object conditionValue) {
        try {
            double fieldNum = Double.parseDouble(fieldValue.toString());
            double conditionNum = Double.parseDouble(conditionValue.toString());
            return Double.compare(fieldNum, conditionNum);
        } catch (NumberFormatException e) {
            log.warn("Cannot compare non-numeric values: {} and {}", fieldValue, conditionValue);
            return 0;
        }
    }

    private List<ActionDto.SingleAction> parseActions(String actionsJson) {
        try {
            ActionDto actionDto = objectMapper.readValue(actionsJson, ActionDto.class);
            return actionDto.getActions() != null ? actionDto.getActions() : new ArrayList<>();
        } catch (JsonProcessingException e) {
            log.error("Error parsing actions JSON: {}", actionsJson, e);
            return new ArrayList<>();
        }
    }

    public Map<String, List<RuleEntity>> getCacheStatus() {
        Map<String, List<RuleEntity>> status = new HashMap<>();
        ruleCache.forEach((product, rules) -> {
            status.put(product, new ArrayList<>(rules));
        });
        return status;
    }
} 