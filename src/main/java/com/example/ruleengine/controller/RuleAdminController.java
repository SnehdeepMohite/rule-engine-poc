package com.example.ruleengine.controller;

import com.example.ruleengine.dto.PaginatedResponse;
import com.example.ruleengine.entity.RuleEntity;
import com.example.ruleengine.service.RuleAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/admin/rules")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:3000"})
public class RuleAdminController {

    private static final Logger log = LoggerFactory.getLogger(RuleAdminController.class);

    private RuleAdminService ruleAdminService;

    // Constructor for dependency injection
    public RuleAdminController(RuleAdminService ruleAdminService) {
        this.ruleAdminService = ruleAdminService;
    }
    private Map<String, List<RuleEntity>> ruleCache = new ConcurrentHashMap<>();

    @GetMapping
    public ResponseEntity<List<RuleEntity>> getAllRules() {
        log.info("Getting all rules");
        List<RuleEntity> rules = ruleAdminService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/paginated")
    public ResponseEntity<PaginatedResponse<RuleEntity>> getAllRulesPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Getting paginated rules - page: {}, size: {}, sortBy: {}, sortDirection: {}", page, size, sortBy, sortDirection);
        PaginatedResponse<RuleEntity> response = ruleAdminService.getAllRulesPaginated(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<RuleEntity>> searchRulesPaginated(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Searching rules - name: {}, product: {}, enabled: {}, page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                name, product, enabled, page, size, sortBy, sortDirection);
        PaginatedResponse<RuleEntity> response = ruleAdminService.searchRulesPaginated(name, product, enabled, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{product}")
    public ResponseEntity<List<RuleEntity>> getRulesByProduct(@PathVariable String product) {
        log.info("Getting rules for product: {}", product);
        List<RuleEntity> rules = ruleAdminService.getRulesByProduct(product);
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/product/{product}/paginated")
    public ResponseEntity<PaginatedResponse<RuleEntity>> getRulesByProductPaginated(
            @PathVariable String product,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Getting paginated rules for product: {} - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                product, page, size, sortBy, sortDirection);
        PaginatedResponse<RuleEntity> response = ruleAdminService.getRulesByProductPaginated(product, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{enabled}")
    public ResponseEntity<PaginatedResponse<RuleEntity>> getRulesByEnabledStatusPaginated(
            @PathVariable boolean enabled,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Getting paginated rules by enabled status: {} - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                enabled, page, size, sortBy, sortDirection);
        PaginatedResponse<RuleEntity> response = ruleAdminService.getRulesByEnabledStatusPaginated(enabled, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuleEntity> getRuleById(@PathVariable UUID id) {
        log.info("Getting rule by id: {}", id);
        return ruleAdminService.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RuleEntity> createRule(@RequestBody RuleEntity rule) {
        log.info("Creating new rule: {}", rule.getName());
        try {
            RuleEntity createdRule = ruleAdminService.createRule(rule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while creating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating rule: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleEntity> updateRule(@PathVariable UUID id, @RequestBody RuleEntity rule) {
        log.info("Updating rule with id: {}", id);
        try {
            RuleEntity updatedRule = ruleAdminService.updateRule(id, rule);
            return ResponseEntity.ok(updatedRule);
        } catch (IllegalArgumentException e) {
            log.error("Validation error while updating rule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating rule: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        log.info("Deleting rule with id: {}", id);
        try {
            ruleAdminService.deleteRule(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting rule: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting rule: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<RuleEntity> toggleRuleStatus(@PathVariable UUID id) {
        log.info("Toggling rule status for id: {}", id);
        try {
            ruleAdminService.toggleRuleStatus(id);
            return ruleAdminService.getRuleById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.error("Error toggling rule status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error toggling rule status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh-cache")
    public ResponseEntity<Void> refreshCache() {
        log.info("Refreshing rule cache");
        try {
            ruleAdminService.refreshCache();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error refreshing cache: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 