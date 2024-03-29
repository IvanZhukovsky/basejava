package com.urise.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private List<Organization> organizations;
    private static final long serialVersionUID = 1L;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }
    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void deleteOrganization(String name) {
        for (int i = 0; i < getOrganizations().size(); i++) {
            Link homePage = getOrganizations().get(i).getHomePage();
            if (homePage.getName().equals(name)) {
                getOrganizations().remove(i);
                break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        String textGroup = "";
        for (Organization organization : organizations) {
            textGroup = textGroup + organization.toString() +  "\n";
        }
        return textGroup;
    }
}
