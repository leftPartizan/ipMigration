package com.ipmigration.parsing;

import org.apache.commons.net.util.SubnetUtils;

import java.util.*;


public class Subnet {

    private Integer net;
    private Integer id;
    private SubnetUtils.SubnetInfo subnetInfo;
    private String address;
    private String cidr;
    private int prefixMask;
    private int lowIp;
    private int highIp;
    private Map<String, Integer> listIpRouter = new HashMap<>();
    private TreeSet<Subnet> listAddressSubnets = new TreeSet<>(Subnet::compareTo);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNet() { return net; }

    public void setNet(Integer net) {
        this.net = net;
    }

    public Subnet(String name) {
        subnetInfo = new SubnetUtils(name).getInfo();
        address = subnetInfo.getAddress();
        cidr = subnetInfo.getCidrSignature();
        prefixMask = Integer.parseInt(cidr.split("/")[1]);
        highIp = subnetInfo.asInteger(subnetInfo.getHighAddress());
        lowIp = subnetInfo.asInteger(subnetInfo.getHighAddress());
    }

    public Subnet(String name, Integer id) {
        this(name);
        this.id = id;
    }

    public Subnet() {}

    public int getPrefixMask() {
        return prefixMask;
    }

    public boolean isInRange(String address) {
        return subnetInfo.isInRange(address);
    }

    public boolean isInRange(Subnet otherSubnet) {
        if (this.address.equals(otherSubnet.address)) {
            if (this.prefixMask < otherSubnet.getPrefixMask()) {
                return true;
            }
            if (this.prefixMask > otherSubnet.getPrefixMask()) {
                return false;
            }
            if(this.prefixMask == otherSubnet.getPrefixMask()) {
                return false;
            }
            return false;
        }
        else {
            return this.subnetInfo.isInRange(otherSubnet.getAddress());
        }
    }

    public String getAddress() {
        return address;
    }

    public String getCidr() {
        return cidr;
    }

    public TreeSet<Subnet> getListAddressSubnets() {
        return listAddressSubnets;
    }

    @Override
    public String toString() {
        return "Subnet{" +
                "net=" + net +
                ", id=" + id +
                ", subnetInfo=" + subnetInfo +
                ", address='" + address + '\'' +
                ", cidr='" + cidr + '\'' +
                ", prefixMask=" + prefixMask +
                ", lowIp=" + lowIp +
                ", highIp=" + highIp +
                ", listIpRouter=" + listIpRouter +
                ", listAddressSubnets=" + listAddressSubnets +
                '}';
    }

    public Map<String, Integer> getMap() {
        return listIpRouter;
    }

    private int getLowIp() { return lowIp; }

    private int getHighIp() { return highIp; }

    public int compareTo(Subnet subnet) {
        if (highIp < subnet.getLowIp()) return 1;
        if (lowIp > subnet.getHighIp()) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subnet subnet = (Subnet) o;
        return Objects.equals(cidr, subnet.cidr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidr);
    }
}
