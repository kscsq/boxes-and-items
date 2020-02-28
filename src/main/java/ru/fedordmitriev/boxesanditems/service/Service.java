package ru.fedordmitriev.boxesanditems.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.fedordmitriev.boxesanditems.entity.Box;
import ru.fedordmitriev.boxesanditems.entity.Item;
import ru.fedordmitriev.boxesanditems.entity.Storage;
import ru.fedordmitriev.boxesanditems.repository.BoxRepository;
import ru.fedordmitriev.boxesanditems.repository.ItemRepository;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@org.springframework.stereotype.Service
@EnableConfigurationProperties
public class Service {

    private static final Logger log = LoggerFactory.getLogger(Service.class);

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${csv.file.box}")
    private String boxCsvFile;

    @Value("${csv.file.item}")
    private String itemCsvFile;

    /**
     * Возвращает заполненный объект Storage
     *
     * @param name строка с адресом xml файла
     * @return объект Storage
     * @throws IOException
     */
    public Storage read(String name) throws IOException {
        File file = new File(name);
        String xml = inputStreamToString(new FileInputStream(file));
        XmlMapper mapper = new XmlMapper();

        return mapper.readValue(xml, Storage.class);
    }

    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * Сохранение в базу данных из модели
     *
     * @param storageModel
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Storage storageModel) {
        saveBoxes(storageModel.getBoxes(), null);
        saveItems(storageModel.getItems(), null);
    }

    private void saveBoxes(List<Box> boxes, Box parentBox) {
        for (Box box : boxes) {
            if (box.getBoxesList() != null && !box.getBoxesList().isEmpty()) {
                saveBoxes(box.getBoxesList(), box);
                boxRepository.save(box);
            } else {
                box.setParentBox(parentBox);
                boxRepository.save(box);
            }
            if (box.getItemsList() != null && !box.getItemsList().isEmpty()) {
                box.setParentBox(parentBox);
                saveItems(box.getItemsList(), box);
            }
        }
    }

    private void saveItems(List<Item> items, Box parentBox) {
        for (Item item : items) {
            item.setParentBox(parentBox);
            itemRepository.save(item);
        }
    }

    public void exportToFile() {
        jdbcTemplate.execute("call CSVWRITE ( '" + boxCsvFile + "', 'SELECT * FROM box; SELECT * FROM item', 'charset=UTF-8' )");
        jdbcTemplate.execute("call CSVWRITE ( '" + itemCsvFile + "', 'SELECT * FROM item', 'charset=UTF-8' )");
    }

    public Set<Long> findAllItemsInBoxWithColor(Long id, String color) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        log.info(optionalItem.isPresent() ? "" + optionalItem.get() + "\n" : "no such item\n");
        Optional<Box> optionalBox = boxRepository.findById(id);
        if (!optionalBox.isPresent()) {
            log.info("no boxes with id = {}", id);
            return new HashSet<>();
        }
        Box parentBox = optionalBox.get();
        Set<Long> itemIds = new HashSet<>();

        getAllItems(parentBox, itemIds, color);
        for (Long itemId : itemIds) {
            log.info("i {}", itemId);
        }
        return itemIds;
    }

    private void getAllItems(Box parentBox, Set<Long> items, String color) {
        for (Item item : parentBox.getItemsList()) {
            if (color.equals(item.getColor()))
                items.add(item.getId());
        }
        for (Box box : parentBox.getBoxesList()) {
            getAllItems(box, items, color);
        }
    }

}
