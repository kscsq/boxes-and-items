package ru.fedordmitriev.boxesanditems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.fedordmitriev.boxesanditems.entity.Storage;
import ru.fedordmitriev.boxesanditems.service.Service;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BoxesAndItemsApplication {

    @Autowired
    private Service myService;

    private static ConfigurableApplicationContext context;
    private static final Logger log = LoggerFactory.getLogger(BoxesAndItemsApplication.class);

    public static void main(String[] args) {
        log.info("Application started");
        context = SpringApplication.run(BoxesAndItemsApplication.class, args);
        BoxesAndItemsApplication app1 = (BoxesAndItemsApplication) context.getBean("boxesAndItemsApplication");
        app1.start(args);
        app1.exportToFile();
    }

    private void exportToFile() {
        myService.exportToFile();
    }

    public void start(String[] args) {
        Storage storageModel = null;
        try {
            storageModel = myService.read(validateAndRetrieveFileName(args));
        } catch (IOException e) {
            log.error("IOException Error");
            closeContext();
        }

        if (storageModel != null) {
            myService.save(storageModel);
        }
    }

    private void closeContext() {
        context.close();
    }

    private String validateAndRetrieveFileName(String[] args) {
        if (args == null || args.length == 0) {
            log.error("No file path provided");
            closeContext();
        } else if (!isFile(args[0])) {
            log.error("Provided arg could not be resolved to a file");
            closeContext();
        }
        return args[0];
    }

    private boolean isFile(String arg) {
        File file = new File(arg);
        return file.isFile();
    }
}
