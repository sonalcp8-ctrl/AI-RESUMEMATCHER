package com.resumematcher.repository;

import com.resumematcher.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserIdOrderByAppliedDateDesc(Long userId);
    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
