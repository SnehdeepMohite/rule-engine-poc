package com.example.ruleengine.repository;

import com.example.ruleengine.entity.RuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, UUID>, PagingAndSortingRepository<RuleEntity, UUID> {

    @Query("SELECT r FROM RuleEntity r WHERE r.product = :product AND r.enabled = true ORDER BY r.priority ASC")
    List<RuleEntity> findEnabledRulesByProductOrderByPriority(@Param("product") String product);

    @Query("SELECT r FROM RuleEntity r WHERE r.enabled = true ORDER BY r.priority ASC")
    List<RuleEntity> findAllEnabledRulesOrderByPriority();

    @Query("SELECT r FROM RuleEntity r WHERE r.product = :product ORDER BY r.priority ASC")
    List<RuleEntity> findAllRulesByProductOrderByPriority(@Param("product") String product);

    // Pagination methods
    Page<RuleEntity> findAll(Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE r.product = :product")
    Page<RuleEntity> findAllByProduct(@Param("product") String product, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE r.enabled = :enabled")
    Page<RuleEntity> findAllByEnabled(@Param("enabled") boolean enabled, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE r.product = :product AND r.enabled = :enabled")
    Page<RuleEntity> findAllByProductAndEnabled(@Param("product") String product, @Param("enabled") boolean enabled, Pageable pageable);

    // Search methods
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<RuleEntity> findAllByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.product) LIKE LOWER(CONCAT('%', :product, '%'))")
    Page<RuleEntity> findAllByProductContainingIgnoreCase(@Param("product") String product, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(r.product) LIKE LOWER(CONCAT('%', :product, '%'))")
    Page<RuleEntity> findAllByNameContainingIgnoreCaseAndProductContainingIgnoreCase(@Param("name") String name, @Param("product") String product, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND r.enabled = :enabled")
    Page<RuleEntity> findAllByNameContainingIgnoreCaseAndEnabled(@Param("name") String name, @Param("enabled") boolean enabled, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.product) LIKE LOWER(CONCAT('%', :product, '%')) AND r.enabled = :enabled")
    Page<RuleEntity> findAllByProductContainingIgnoreCaseAndEnabled(@Param("product") String product, @Param("enabled") boolean enabled, Pageable pageable);
    
    @Query("SELECT r FROM RuleEntity r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(r.product) LIKE LOWER(CONCAT('%', :product, '%')) AND r.enabled = :enabled")
    Page<RuleEntity> findAllByNameContainingIgnoreCaseAndProductContainingIgnoreCaseAndEnabled(@Param("name") String name, @Param("product") String product, @Param("enabled") boolean enabled, Pageable pageable);

    boolean existsByNameAndProduct(String name, String product);
} 