package com.ipmigration.parsing;

import com.ipmigration.model.IncorrectLine;
import com.ipmigration.model.IpAddressRouter;
import com.ipmigration.model.IpRanges;
import com.ipmigration.model.Router;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


public class Parsing {

    private Subnet network = new Subnet();
    private TreeSet<Subnet> listSubNets = new TreeSet<>(Subnet::compareTo);
    private List<Router> listRouters = new ArrayList<>();
    private List<IpAddressRouter> listIpRouters = new ArrayList<>();
    private List<String> listIp = new ArrayList<>();
    private List<IpRanges> listIpRanges = new ArrayList<>();
    private Scanner scanner;
    private List<IncorrectLine> incorrectLines = new ArrayList<>();
    private Set<String> setLines = new HashSet<>();
    private Subnet subnetNotExisting = new Subnet();

    public Parsing(){}

    public Parsing(InputStream file) { createNetwork(file); }

    public Parsing(String path) { createNetwork(path); }

    private void createNetwork(InputStream file) {
        scanner = new Scanner(file);
        createNetwork(scanner);
    }

    private void createNetwork(String path) {
        // считываем файл
        File file = new File(path);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        // парсим файл с данными
        createNetwork(scanner);
    }

    private void createNetwork(Scanner scanner) {
        parsingLine(scanner);
        listSubNets.forEach(x-> createTreeSubnetRecursively(x, network));//
        createIpRanges(); // заполняем таблицу дапозонов айпи сетей
        mapIpToRange(network); // заполняем таблицу айпи адресов роутеров
    }

    private void createIpRanges() { listSubNets.forEach(x -> listIpRanges.add(new IpRanges(x.getId(), x.getCidr(), x.getNet()))); }

    private void mapIpToRange(Subnet subnet) {

        boolean isInRange = false;
        int id_router = -1;
        for (String ip : listIp) {
            id_router++;
            for (Subnet subnetInList : subnet.getListAddressSubnets()) {
                if (subnetInList.isInRange(ip)) {
                    mapIpToRange(subnetInList, ip, id_router);
                    isInRange = true;
                    break;
                }
            }
            if (!isInRange) {
                listIpRouters.add(new IpAddressRouter(id_router, ip, subnet.getId()));
            } else {
                isInRange = false;
            }

        }
    }

    private void mapIpToRange(Subnet subnet, String ip, int id_router) {

        TreeSet<Subnet> listSubnets = subnet.getListAddressSubnets();

        if (listSubnets.size() == 0) {
            listIpRouters.add(new IpAddressRouter(id_router, ip, subnet.getId()));
        } else {
            boolean isInRange = false;
            for (Subnet subnetInList : listSubnets) {
                if (subnetInList.isInRange(ip)) {
                    mapIpToRange(subnetInList, ip, id_router);
                    isInRange = true;
                    break;
                }
            }
            if (!isInRange) {
                listIpRouters.add(new IpAddressRouter(id_router, ip, subnet.getId()));
            } else {
                isInRange = false;
            }
        }
    }

    public void createTreeSubnetRecursively(Subnet subnet, Subnet otherSubnet) {
        // если subnet входит диапозон сети otherSubnet и его подстити, то
        // добавлем subnet в otherSubnet или его подсеть рекурсивно

        // список сетей входящий в диапозон сети otherSubnet
        TreeSet<Subnet> listSubNets = otherSubnet.getListAddressSubnets();

        //проверяем наличие в сети других подситей
        if (listSubNets.size() == 0) {
            listSubNets.add(subnet);
            subnet.setNet(otherSubnet.getId()); // сохраняем в поле обьекта подсети ид сети в которой он находится
        } else {
            int count = 0;
            for (Subnet subnetInList : listSubNets) {
                if (subnetInList.getCidr().equals(subnet.getCidr())) {
                    count = -1;
                    break;
                }

                if (subnetInList.isInRange(subnet)) {
                    count = 1;
                    createTreeSubnetRecursively(subnet, subnetInList);
                    break;
                }
            }

            // если число count равна размеру списка подсетей сети otherSubnet, то
            // подсеть subnet не входит в диапозон сетей этого списка
            if (count == 0) {
                // проверим входит ли в диапозон сети subnet
                // подсети сети otherSubnet
                Subnet subnetForRemove = null;
                for (Subnet subnetInlist : listSubNets) {
                    if (subnet.isInRange(subnetInlist)) {
                        createTreeSubnetRecursively(subnetInlist, subnet);
                        subnetForRemove = subnetInlist;
                        break;
                    }
                }
                listSubNets.add(subnet);
                subnet.setNet(otherSubnet.getId());
                if (subnetForRemove != null ) {
                    listSubNets.remove(subnetForRemove);
                }
            }
        }
    }

   @Deprecated
    private void updateChainsSubnets(TreeSet<Subnet> listSubNets) {
        // проверяем наличие подсетей, которые не попали свои диапозоны адресов
        // при первом формировании иерархии сетей и добавляем их туда
        for (Subnet subnet : listSubNets) {
            for (Subnet subnet2 : listSubNets) {
                if (subnet.getId().intValue() != subnet.getId().intValue()) {
                    if (subnet.isInRange(subnet2)) {
                        createTreeSubnetRecursively(subnet2, subnet);
                        listSubNets.remove(subnet2);
                        continue;
                    }
                    if (subnet2.isInRange(subnet)) {
                        createTreeSubnetRecursively(subnet, subnet2);
                        listSubNets.remove(subnet);
                        break;
                    }
                }
            }
        }
    }

    public List<Router> getListRouters() { return listRouters; }

    public List<IpAddressRouter> getListIpRouters() { return listIpRouters; }

    public List<IpRanges> getListIpRanges() { return listIpRanges; }

    public List<IncorrectLine> getIncorrectLines() { return incorrectLines; }

    public void parsingLine(Scanner scanner) {
        String reg = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])" ;

        String regIP = reg + "$";
        String regCidr = reg + "(/([0-9]|[1-2][0-9]|3[0-2]))$";
        int countSubnet = 0;
        int countRouter = 0;
        int countLine = 0;
        while (scanner.hasNext()) {
            countLine++;
            String line = scanner.next();
            if (line.contains(";")) {
                if (setLines.add(line)) {
                    if (line.split(";")[0].matches(regIP)) {
                        String[] lineWithRouter = line.split(";");
                        String routerIP = lineWithRouter[0];
                        String routerName = lineWithRouter[1];
                        // заполняем таблицу роутеров
                        listRouters.add(new Router(routerName, countRouter));
                        listIp.add(routerIP);
                        countRouter++;
                    }
                }
                else { incorrectLines.add(new IncorrectLine(countLine, line)); }
            } else if (line.contains("/")){
                if (line.matches(regCidr)) {
                    if (listSubNets.add(new Subnet(line, countSubnet))) {
                        countSubnet++;
                    }
                }
                else { incorrectLines.add(new IncorrectLine(countLine, line)); }
            } else { incorrectLines.add(new IncorrectLine(countLine, line)); }
        }
    }
}