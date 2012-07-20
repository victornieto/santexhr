package org.openapplicant.domain;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.NotEmpty;
import org.openapplicant.util.Strings;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;

/**
 * User: Gian Franco Zabarino
 * Date: 7/5/12
 * Time: 10:06 AM
 */
@Entity
public class JobPosition extends DomainObject {
    private String name;
    private Company company;
    private Set<Seniority> seniorities;

    @Column(nullable = false)
    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = false)
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @CollectionOfElements
    @Enumerated(value = EnumType.STRING)
    public Set<Seniority> getSeniorities() {
        return seniorities;
    }

    public void setSeniorities(Set<Seniority> seniorities) {
        this.seniorities = seniorities;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(name);
        if (seniorities.size() > 1) {
            s.append(" - [");
            Seniority[] orderedSeniorities = seniorities.toArray(new Seniority[seniorities.size()]);
            Arrays.sort(orderedSeniorities);
            for (Seniority seniority : orderedSeniorities) {
                s.append(Strings.humanize(seniority.name(), true));
                s.append(", ");
            }
            s.delete(s.length() - 2, s.length());
            s.append("]");
        } else if (seniorities.size() == 1) {
            s.append(" - ").append(seniorities.iterator().next());
        }
        return s.toString();
    }

    @Transient
    public String getTitle() {
        return toString();
    }
}
