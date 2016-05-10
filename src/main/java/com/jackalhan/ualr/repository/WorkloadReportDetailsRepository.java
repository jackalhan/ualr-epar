package com.jackalhan.ualr.repository;

import com.jackalhan.ualr.domain.WorkloadReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Repository
public interface WorkloadReportDetailsRepository extends JpaRepository<WorkloadReportDetails, Long> {

}
