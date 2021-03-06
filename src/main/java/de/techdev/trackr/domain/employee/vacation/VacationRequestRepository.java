package de.techdev.trackr.domain.employee.vacation;

import de.techdev.trackr.domain.employee.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

/**
 * @author Moritz Schulze
 */
public interface VacationRequestRepository extends CrudRepository<VacationRequest, Long> {

    @Override
    @PostAuthorize("hasRole('ROLE_SUPERVISOR') or principal?.id == returnObject.employee.id")
    VacationRequest findOne(Long aLong);

    @Override
    @RestResource(exported = false)
    Iterable<VacationRequest> findAll();

    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or principal?.id == #employee.id")
    List<VacationRequest> findByEmployeeOrderByStartDateAsc(@Param("employee") Employee employee);

    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    List<VacationRequest> findByStatusOrderBySubmissionTimeAsc(@Param("status") VacationRequest.VacationRequestStatus status);

    @RestResource(exported = false)
    List<VacationRequest> findBySubmissionTimeBeforeAndStatus(Date date, VacationRequest.VacationRequestStatus status);

    /**
     * Find vacation requests of a certain status that overlap with a period.
     */
    @RestResource(exported = false)
    List<VacationRequest> findByStartDateBetweenOrEndDateBetweenAndStatus(
            Date startLower, Date startHigher,
            Date endLower, Date endHigher,
            VacationRequest.VacationRequestStatus status);
}
