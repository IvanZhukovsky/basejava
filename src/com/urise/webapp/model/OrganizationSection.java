package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private final ArrayList<Organization> organizations = new ArrayList<>();

    public ArrayList<Organization> getOrganizations() {
        return organizations;
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
