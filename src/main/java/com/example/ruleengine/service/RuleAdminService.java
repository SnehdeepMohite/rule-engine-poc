package com.example.ruleengine.service;

import com.example.ruleengine.dto.ActionDto;
import com.example.ruleengine.dto.ConditionDto;
import com.example.ruleengine.dto.PaginatedResponse;
import com.example.ruleengine.engine.RuleEngine;
import com.example.ruleengine.entity.RuleEntity;
import com.example.ruleengine.repository.RuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RuleAdminService {

    private static final Logger log = LoggerFactory.getLogger(RuleAdminService.class);

    private RuleRepository ruleRepository;

    // Constructor for dependency injection
    public RuleAdminService(RuleRepository ruleRepository, RuleEngine ruleEngine, ObjectMapper objectMapper) {
        this.ruleRepository = ruleRepository;
        this.ruleEngine = ruleEngine;
        this.objectMapper = objectMapper;
    }
    private RuleEngine ruleEngine;
    private ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<RuleEntity> getAllRules() {
        return ruleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<RuleEntity> getAllRulesPaginated(int page, int size, String sortBy, String sortDirection) {
        // Default values
        page = Math.max(0, page - 1); // Convert to 0-based indexing
        size = Math.max(1, Math.min(100, size)); // Limit size between 1 and 100
        sortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "name" : sortBy;
        sortDirection = (sortDirection == null || sortDirection.trim().isEmpty()) ? "asc" : sortDirection.toLowerCase();

        // Validate sortBy field
        if (!isValidSortField(sortBy)) {
            sortBy = "name";
        }

        // Create sort object
        Sort sort = sortDirection.equals("desc") ? 
            Sort.by(Sort.Direction.DESC, sortBy) : 
            Sort.by(Sort.Direction.ASC, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get paginated data
        Page<RuleEntity> rulePage = ruleRepository.findAll(pageable);

        return new PaginatedResponse<>(
            rulePage.getContent(),
            page + 1, // Convert back to 1-based indexing for response
            size,
            rulePage.getTotalElements(),
            rulePage.getTotalPages(),
            sortBy,
            sortDirection
        );
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<RuleEntity> searchRulesPaginated(String name, String product, Boolean enabled, int page, int size, String sortBy, String sortDirection) {
        // Default values
        page = Math.max(0, page - 1); // Convert to 0-based indexing
        size = Math.max(1, Math.min(100, size)); // Limit size between 1 and 100
        sortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "name" : sortBy;
        sortDirection = (sortDirection == null || sortDirection.trim().isEmpty()) ? "asc" : sortDirection.toLowerCase();

        // Validate sortBy field
        if (!isValidSortField(sortBy)) {
            sortBy = "name";
        }

        // Create sort object
        Sort sort = sortDirection.equals("desc") ? 
            Sort.by(Sort.Direction.DESC, sortBy) : 
            Sort.by(Sort.Direction.ASC, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Normalize search parameters
        name = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        product = (product != null && !product.trim().isEmpty()) ? product.trim() : null;

        Page<RuleEntity> rulePage;

        // Determine which search method to use based on provided parameters
        if (name != null && product != null && enabled != null) {
            // Search by name, product, and enabled status
            rulePage = ruleRepository.findAllByNameContainingIgnoreCaseAndProductContainingIgnoreCaseAndEnabled(name, product, enabled, pageable);
        } else if (name != null && product != null) {
            // Search by name and product
            rulePage = ruleRepository.findAllByNameContainingIgnoreCaseAndProductContainingIgnoreCase(name, product, pageable);
        } else if (name != null && enabled != null) {
            // Search by name and enabled status
            rulePage = ruleRepository.findAllByNameContainingIgnoreCaseAndEnabled(name, enabled, pageable);
        } else if (product != null && enabled != null) {
            // Search by product and enabled status
            rulePage = ruleRepository.findAllByProductContainingIgnoreCaseAndEnabled(product, enabled, pageable);
        } else if (name != null) {
            // Search by name only
            rulePage = ruleRepository.findAllByNameContainingIgnoreCase(name, pageable);
        } else if (product != null) {
            // Search by product only
            rulePage = ruleRepository.findAllByProductContainingIgnoreCase(product, pageable);
        } else if (enabled != null) {
            // Search by enabled status only
            rulePage = ruleRepository.findAllByEnabled(enabled, pageable);
        } else {
            // No search parameters, return all rules
            rulePage = ruleRepository.findAll(pageable);
        }

        return new PaginatedResponse<>(
            rulePage.getContent(),
            page + 1, // Convert back to 1-based indexing for response
            size,
            rulePage.getTotalElements(),
            rulePage.getTotalPages(),
            sortBy,
            sortDirection
        );
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<RuleEntity> getRulesByProductPaginated(String product, int page, int size, String sortBy, String sortDirection) {
        // Default values
        page = Math.max(0, page - 1); // Convert to 0-based indexing
        size = Math.max(1, Math.min(100, size)); // Limit size between 1 and 100
        sortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "name" : sortBy;
        sortDirection = (sortDirection == null || sortDirection.trim().isEmpty()) ? "asc" : sortDirection.toLowerCase();

        // Validate sortBy field
        if (!isValidSortField(sortBy)) {
            sortBy = "name";
        }

        // Create sort object
        Sort sort = sortDirection.equals("desc") ? 
            Sort.by(Sort.Direction.DESC, sortBy) : 
            Sort.by(Sort.Direction.ASC, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get paginated data
        Page<RuleEntity> rulePage = ruleRepository.findAllByProduct(product, pageable);

        return new PaginatedResponse<>(
            rulePage.getContent(),
            page + 1, // Convert back to 1-based indexing for response
            size,
            rulePage.getTotalElements(),
            rulePage.getTotalPages(),
            sortBy,
            sortDirection
        );
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<RuleEntity> getRulesByEnabledStatusPaginated(boolean enabled, int page, int size, String sortBy, String sortDirection) {
        // Default values
        page = Math.max(0, page - 1); // Convert to 0-based indexing
        size = Math.max(1, Math.min(100, size)); // Limit size between 1 and 100
        sortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "name" : sortBy;
        sortDirection = (sortDirection == null || sortDirection.trim().isEmpty()) ? "asc" : sortDirection.toLowerCase();

        // Validate sortBy field
        if (!isValidSortField(sortBy)) {
            sortBy = "name";
        }

        // Create sort object
        Sort sort = sortDirection.equals("desc") ? 
            Sort.by(Sort.Direction.DESC, sortBy) : 
            Sort.by(Sort.Direction.ASC, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get paginated data
        Page<RuleEntity> rulePage = ruleRepository.findAllByEnabled(enabled, pageable);

        return new PaginatedResponse<>(
            rulePage.getContent(),
            page + 1, // Convert back to 1-based indexing for response
            size,
            rulePage.getTotalElements(),
            rulePage.getTotalPages(),
            sortBy,
            sortDirection
        );
    }

    @Transactional(readOnly = true)
    public List<RuleEntity> getRulesByProduct(String product) {
        return ruleRepository.findAllRulesByProductOrderByPriority(product);
    }

    @Transactional(readOnly = true)
    public Optional<RuleEntity> getRuleById(UUID id) {
        return ruleRepository.findById(id);
    }

    @Transactional
    public RuleEntity createRule(RuleEntity rule) {
        validateRule(rule);
        
        // Check for duplicate name in same product
        if (ruleRepository.existsByNameAndProduct(rule.getName(), rule.getProduct())) {
            throw new IllegalArgumentException("Rule with name '" + rule.getName() + "' already exists for product '" + rule.getProduct() + "'");
        }

        RuleEntity savedRule = ruleRepository.save(rule);
        log.info("Created rule: {}", savedRule.getName());
        
        // Refresh cache after creation
        ruleEngine.refreshCache();
        
        return savedRule;
    }

    @Transactional
    public RuleEntity updateRule(UUID id, RuleEntity ruleUpdate) {
        Optional<RuleEntity> existingRule = ruleRepository.findById(id);
        if (existingRule.isEmpty()) {
            throw new IllegalArgumentException("Rule not found with id: " + id);
        }

        RuleEntity existing = existingRule.get();
        
        // Check for duplicate name in same product (excluding current rule)
        if (!existing.getName().equals(ruleUpdate.getName()) && 
            ruleRepository.existsByNameAndProduct(ruleUpdate.getName(), ruleUpdate.getProduct())) {
            throw new IllegalArgumentException("Rule with name '" + ruleUpdate.getName() + "' already exists for product '" + ruleUpdate.getProduct() + "'");
        }

        validateRule(ruleUpdate);
        
        // Update fields
        existing.setProduct(ruleUpdate.getProduct());
        existing.setName(ruleUpdate.getName());
        existing.setDescription(ruleUpdate.getDescription());
        existing.setPriority(ruleUpdate.getPriority());
        existing.setConditions(ruleUpdate.getConditions());
        existing.setActions(ruleUpdate.getActions());
        existing.setEnabled(ruleUpdate.getEnabled());
        existing.setVersion(existing.getVersion() + 1);

        RuleEntity updatedRule = ruleRepository.save(existing);
        log.info("Updated rule: {}", updatedRule.getName());
        
        // Refresh cache after update
        ruleEngine.refreshCache();
        
        return updatedRule;
    }

    @Transactional
    public void deleteRule(UUID id) {
        Optional<RuleEntity> rule = ruleRepository.findById(id);
        if (rule.isEmpty()) {
            throw new IllegalArgumentException("Rule not found with id: " + id);
        }

        String ruleName = rule.get().getName();
        ruleRepository.deleteById(id);
        log.info("Deleted rule: {}", ruleName);
        
        // Refresh cache after deletion
        ruleEngine.refreshCache();
    }

    @Transactional
    public void toggleRuleStatus(UUID id) {
        Optional<RuleEntity> rule = ruleRepository.findById(id);
        if (rule.isEmpty()) {
            throw new IllegalArgumentException("Rule not found with id: " + id);
        }

        RuleEntity existing = rule.get();
        existing.setEnabled(!existing.getEnabled());
        existing.setVersion(existing.getVersion() + 1);

        ruleRepository.save(existing);
        log.info("Toggled rule status: {} -> {}", existing.getName(), existing.getEnabled());
        
        // Refresh cache after status change
        ruleEngine.refreshCache();
    }

    private void validateRule(RuleEntity rule) {
        if (rule.getProduct() == null || rule.getProduct().trim().isEmpty()) {
            throw new IllegalArgumentException("Product is required");
        }
        
        if (rule.getName() == null || rule.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (rule.getPriority() == null || rule.getPriority() < 0) {
            throw new IllegalArgumentException("Priority must be a positive number");
        }
        
        if (rule.getConditions() == null || rule.getConditions().trim().isEmpty()) {
            throw new IllegalArgumentException("Conditions are required");
        }
        
        if (rule.getActions() == null || rule.getActions().trim().isEmpty()) {
            throw new IllegalArgumentException("Actions are required");
        }

        // Validate JSON format for conditions
        try {
            objectMapper.readValue(rule.getConditions(), ConditionDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid conditions JSON format: " + e.getMessage());
        }

        // Validate JSON format for actions
        try {
            objectMapper.readValue(rule.getActions(), ActionDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid actions JSON format: " + e.getMessage());
        }
    }

    private boolean isValidSortField(String sortBy) {
        if (sortBy == null) return false;
        
        String[] validFields = {"id", "name", "product", "priority", "enabled", "version", "createdAt", "updatedAt"};
        for (String field : validFields) {
            if (field.equalsIgnoreCase(sortBy)) {
                return true;
            }
        }
        return false;
    }

    public void refreshCache() {
        ruleEngine.refreshCache();
    }
} 