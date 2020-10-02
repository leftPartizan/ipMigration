package com.ipmigration;

import com.ipmigration.dao.*;
import com.ipmigration.model.*;
import com.ipmigration.parsing.Parsing;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

@RestController
public class MyController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private IpRangesRepository rangesRepository;
    private IpAddressRouterRepository addressRouterRepository;
    private RouterRepository routerRepository;
    private IncorrectLineRepository incorrectLineRepository;
    private DefaultValidationCommandRepository defaultValidationCommandRepository;
    private CommandLinesRepository commandLinesRepository;
    private ValidationRepository validationRepository;
    private ObjectRepository objectRepository;

    public MyController(IpRangesRepository rangesRepository, IpAddressRouterRepository addressRouterRepository,
                        RouterRepository routerRepository, IncorrectLineRepository incorrectLineRepository, DefaultValidationCommandRepository defaultValidationCommandRepository, CommandLinesRepository commandLinesRepository, ValidationRepository validationRepository, ObjectRepository objectRepository) {
        this.rangesRepository = rangesRepository;
        this.addressRouterRepository = addressRouterRepository;
        this.routerRepository = routerRepository;
        this.incorrectLineRepository = incorrectLineRepository;
        this.defaultValidationCommandRepository = defaultValidationCommandRepository;
        this.commandLinesRepository = commandLinesRepository;
        this.validationRepository = validationRepository;
        this.objectRepository = objectRepository;
    }

    // загрузка файла, его парсинг и заполнение SDB таблиц
    @PostMapping(value = "/load")
    @ResponseBody
    public ResponseEntity loadFile(@RequestParam("file") MultipartFile uploadFile) {

        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            System.out.println("EROOR     AAAAAAAAAAAAA");
            e.printStackTrace();
        }
        new SimpleJdbcCall(jdbcTemplate).withProcedureName("CLEAR_SDB_TABLES").execute(); // очищаем таблицы
        Parsing parsing = new Parsing(inputStream); // парсинг файла
        routerRepository.saveAll(parsing.getListRouters()); // сохраняем полученные записи роутеров в таблицу SDB_ROUTERS
        rangesRepository.saveAll(parsing.getListIpRanges()); // сохраняем полученные записи айпи роутеров в таблицу SDB_IP_ROUTERS
        addressRouterRepository.saveAll(parsing.getListIpRouters()); // сохраняем полученные записи айпи диапозонов в таблицу SDB_IP_RANGES
        incorrectLineRepository.saveAll(parsing.getIncorrectLines()); // сохраняем строки не пршедших индетификацию в таблицу SDB_INCORRECT_LINE
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    //  загрузить общую информацию о валидированных данных
    @PostMapping(value = "/loadReport", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loadReport() {
        validationRepository.deleteAll();
        int status = -1;
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).
                 withProcedureName("EXECUTE_COMMANDS");
        call.addDeclaredParameter(new SqlOutParameter("status", OracleTypes.NUMBER));
        Map a =  call.execute(new HashMap<String, Integer>().put("status", status));
        BigDecimal b = (BigDecimal) a.get("status");
        status = b.intValue();
        if (status != -1) { // если возникла ошибка, то показываем страницу с сообщением об ошибке
            Optional<CommandLines> command = commandLinesRepository.findById(status);
            Map<Integer, String> map = new HashMap<>();
            if (command.isPresent()) {
                map.put(status, command.get().getCommand());
            }
            return new ResponseEntity<Map>(map, HttpStatus.BAD_REQUEST);
        } else  {
            Integer countRouter = jdbcTemplate.queryForObject("select * from COUNT_ROUTERS", Integer.class);
            Integer countIpRouter = jdbcTemplate.queryForObject("select * from COUNT_IP_ROUTERS", Integer.class);
            Integer countIpRange = jdbcTemplate.queryForObject("select * from COUNT_RANGES", Integer.class);
            Integer countIncorrectLines = jdbcTemplate.queryForObject("select * from COUNT_INCORRECT_LINES", Integer.class);
            Integer countValidationErrorLines = jdbcTemplate.queryForObject("select * from COUNT_INVALID_OBJECT", Integer.class);
            Integer countInvalidRouter = jdbcTemplate.queryForObject("select * from COUNT_INVALID_IP_ROUTERS", Integer.class);
            Integer countInvalidIpRouter = jdbcTemplate.queryForObject("select * from COUNT_INVALID_IP_ROUTERS", Integer.class);
            Integer countInvalidIpRange = jdbcTemplate.queryForObject("select * from COUNT_INVALID_IP_RANGES", Integer.class);

            Map<String, Integer> map = new HashMap<>();
            map.put("countRouter", countRouter);
            map.put("countIpRouter", countIpRouter);
            map.put("countIpRange", countIpRange);
            map.put("countIncorrectLines", countIncorrectLines);
            map.put("countValidationErrorLines", countValidationErrorLines);
            map.put("countInvalidRouter", countInvalidRouter);
            map.put("countInvalidIpRouter", countInvalidIpRouter);
            map.put("countInvalidIpRange", countInvalidIpRange);
            return new ResponseEntity<Map>(map, HttpStatus.OK);
        }
    }

    //  загрузить валидационые команды
    @PostMapping(value = "/getCommands", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> checkCommandLinesTable() {
        // если таблица с коммандами пуста, то загружаем  дефолтные команды из другой таблицы
        if (commandLinesRepository.count() == 0) {
            return new ResponseEntity<>(defaultValidationCommandRepository.findAllByOrderById(), HttpStatus.SEE_OTHER);
        }
        else {
            return new ResponseEntity<>(commandLinesRepository.findAllByOrderById(), HttpStatus.ACCEPTED);
        }
    }

    // сохранить валидационые команды
    @PostMapping(value = "/saveCommands", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCommands(@RequestBody List<CommandLines> commands) {
        commandLinesRepository.deleteAll();
        commandLinesRepository.saveAll(commands);
        return new ResponseEntity(HttpStatus.OK);
    }

    // загрузить список ошибок
    @PostMapping(value = "/loadValidationTable", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Validation> loadValidationTable() {
        return validationRepository.findAll();
    }

    // загрузить в таблицу object  валидированные данные
    @PostMapping(value = "/saveData", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveData() {
        new SimpleJdbcCall(jdbcTemplate).withProcedureName("LOAD_DATA_FROM_SDB").execute();
    }

    // загрузить записи из таблицы object,  те которые принадлежат определённому обьекту (parent_id)
    @PostMapping(value = "/loadData", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List> loadData(@RequestBody IndexObject index) {

        List<ObjectTable> objects;
        if (index.getId() == null) {
             objects = objectRepository.findObjectToParent_id();
        }
        else {
            objects = objectRepository.findObjectToParent_id(index.getId());
        }
        Map<String, List> lists = new HashMap<>();
        lists.put("listObject",objects);
        lists.put("1", null);
        return lists;
    }

    //загрузить список роутеров, которые принадлжат определённому Ip-адресу
    @PostMapping(value = "/getObjectByReference", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List> getObjectByReference(@RequestBody IndexObject indexObject) {
        List<ObjectTable> objects = objectRepository.findAllByReference(indexObject.getId());
        Map<String, List> lists = new HashMap<>();
        lists.put("listObject",objects);
        return lists;
    }

    // очистить таблицу object и сбросить и авто инкремент
    @PostMapping(value = "/clearObjectTable", produces = MediaType.APPLICATION_JSON_VALUE)
    public void clearObjectTable() {
        new SimpleJdbcCall(jdbcTemplate).withProcedureName("RELOAD_OBJ_TABLE").execute();
    }

    // загрузить в таблицу дефолтные команды
    @PostMapping(value = "/getDefaultCommands", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getDefaultCommands() {
        return new ResponseEntity<>(defaultValidationCommandRepository.findAllByOrderById(), HttpStatus.OK);
    }

    @PostMapping(value = "/clearCommandsTable")
    public void clearCommandsTable() {
        commandLinesRepository.deleteAll();
    }
}
