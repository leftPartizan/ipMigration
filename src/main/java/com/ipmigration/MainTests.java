package com.ipmigration;

import com.ipmigration.parsing.Parsing;

public class MainTests {



    public static void main(String[] args) {


        String path = "D:\\programs\\IdeaProjects\\1migration\\1Project\\";
        String str1 =  "bigData.txt";
        String str2 =  "ranges.txt";
        String str3 =  "r2.txt";
        Parsing parsing = new Parsing(path + str3);
//        System.out.println(parsing.getListIpRanges().get(50).getCidr());
        parsing.getListIpRanges().forEach(x-> System.out.println(x.getInRange_id()));
//        System.out.println(parsing.getListIpRanges().get(parsing.getListIpRanges().get(50).getInRange_id()));
//        parsing.getListIpRanges().forEach(x-> {
//            if (x.getId() < 10 )
//            {System.out.println(x.getCidr());}});


//        Subnet subnet = new Subnet("0.0.0.0/27");
//        Subnet subnet2 = new Subnet("148.101.0.0/16");
//        System.out.println(subnet.compareTo(subnet2));
//        Subnet subnet1 = new Subnet("148.0.0.0/21",1);
//        Subnet subnet11 = new Subnet("148.0.0.0/21",3);
//        Subnet subnet2 = new Subnet("148.0.0.0/20");
//        Subnet subnet3 = new Subnet("148.0.0.0/16");
//        Subnet subnet4 = new Subnet("148.0.0.0/19");

//        System.out.println(str1 > str2);
//        Parsing parsing = new Parsing();
//        TreeSet<Subnet> list = new TreeSet<>(Subnet::compareTo);
//        System.out.println(list.add(subnet1));
//        System.out.println(list.add(subnet11));
//        list.add(subnet2);
//        list.add(subnet3);
//        list.add(subnet4);
//
//        System.out.println(subnet1.equals(subnet11));
//        list.forEach(x-> System.out.println(x.getCidr()));
//        Parsing parsing = new Parsing();
//        list.forEach(s -> parsing.createTreeSubnetRecursively(s, subnet));
//        list.forEach(subnet5 -> System.out.println(subnet5.getCidr()));
//        subnet2.getListAddressSubnets().forEach(x-> System.out.println(x.getCidr()));
//        System.out.println(subnet.isInRange(subnet2.getAddress()));
//        System.out.println(subnet2.isInRange(subnet.getAddress()));
//        System.out.println(subnet.getCidr().equals(subnet2.getCidr()));
//        System.out.println(subnet2.isInRange(subnet));


//        new Parsing("D:\\programs\\IdeaProjects\\1migration\\1Project\\simplyTest222.txt");

    }
}
