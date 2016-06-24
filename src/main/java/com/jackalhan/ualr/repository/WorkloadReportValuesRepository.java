package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jackalhan on 6/16/16.
 */
public interface WorkloadReportValuesRepository extends JpaRepository<WorkloadReportValues, Long> {

    List<WorkloadReportValues> findByWorkloadReportIdOrderBySubjectCodeAscCourseNumberAsc(Long workloadReportId);
}
