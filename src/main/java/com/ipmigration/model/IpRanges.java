package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sdb_ip_ranges")
public class IpRanges {

    @Id
    private int id;
    private String cidr;
    private Integer inRange_id;
    private int is_valid = 1;
    private Integer new_id = null;
    private Integer new_inRange_id = null;

    public IpRanges() {
    }

    public IpRanges(int id, String cidr, Integer inRange_id) {
        this.id = id;
        this.cidr = cidr;
        this.inRange_id = inRange_id;

    }

    @Override
    public String toString() {
        return "IpRanges{" +
                "id=" + id +
                ", cidr='" + cidr + '\'' +
                ", inRange_id=" + inRange_id +
                ", is_valid=" + is_valid +
                ", new_id=" + new_id +
                ", new_inRange_id=" + new_inRange_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public Integer getInRange_id() {
        return inRange_id;
    }

    public void setInRange_id(Integer inRange_id) {
        this.inRange_id = inRange_id;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }
}
