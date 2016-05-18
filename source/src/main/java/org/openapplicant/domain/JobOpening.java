package org.openapplicant.domain;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: Gian Franco Zabarino
 * Date: 7/5/12
 * Time: 10:06 AM
 */
@Entity
public class JobOpening extends DomainObject {
    private final static long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    private Company company;
    private JobPosition jobPosition;
    private String client;
    private Date startDate = new Date();
    private Date finishDate = new Date(System.currentTimeMillis() + MILLIS_IN_A_DAY * 14);
    private Status status = Status.NEW;
    private String description;
    private Set<Candidate> applicants;

    @Transient
    public Boolean isOpen() {
        Date now = new Date();
        return now.before(finishDate) && now.after(startDate);
    }

    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(nullable = false)
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @ManyToOne
    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(optional = false)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Column
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @ManyToMany
    public Set<Candidate> getApplicants() {
        return applicants;
    }

    public void setApplicants(Set<Candidate> applicants) {
        this.applicants = applicants;
    }

    //========================================================================
    // STATUS
    //========================================================================
    public enum Status {
        NEW,
        IN_PROGRESS,
        HIRING_PROCESS,
        CANDIDATE_SELECTED,
        NO_CANDIDATE_SELECTED;

        public static List<Status> getActiveStatus() {
            return Arrays.asList(NEW, IN_PROGRESS, HIRING_PROCESS);
        }

        public static List<Status> getArchivedStatus() {
            return Arrays.asList(CANDIDATE_SELECTED, NO_CANDIDATE_SELECTED);
        }
    }
}
