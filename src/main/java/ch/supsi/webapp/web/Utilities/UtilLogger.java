package ch.supsi.webapp.web.Utilities;

import ch.supsi.webapp.web.Modules.Logs;
import ch.supsi.webapp.web.Repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UtilLogger {

    private static LogsRepository logsRepository;

    public UtilLogger(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }


    private static void log(String message, String level) {
        Logs log = new Logs();
        log.setMessage(message);
        log.setDate(new Date());
        log.setLevel(level);

        logsRepository.save(log);
    }

    public static void info(String message) {
        log(message, "INFO");
    }

    public static void error(String message) {
        log(message, "ERROR");
    }

    public static void warning(String message) {
        log(message, "WARNING");
    }
}
