package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sdb_ip_routers")
public class IpAddressRouter {

    @Id
    private int id_router;
    private String address;
    private Integer ipRange_id;
    private int is_valid = 1;
    private Integer new_id = null;
    private Integer new_ipRange_id = null;

    public IpAddressRouter() {
    }

    public IpAddressRouter(int id_router, String address, Integer ipRange_id) {
        this.id_router = id_router;
        this.address = address;
        this.ipRange_id = ipRange_id;
    }

    @Override
    public String toString() {
        return "IpAddressRouter{" +
                "id_router=" + id_router +
                ", address='" + address + '\'' +
                ", ipRange_id=" + ipRange_id +
                ", is_valid=" + is_valid +
                ", new_id=" + new_id +
                ", new_ipRange_id=" + new_ipRange_id +
                '}';
    }

    public int getId_router() {
        return id_router;
    }

    public void setId_router(int id_router) {
        this.id_router = id_router;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIpRange_id() {
        return ipRange_id;
    }

    public void setIpRange_id(Integer ipRange_id) {
        this.ipRange_id = ipRange_id;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }
}
