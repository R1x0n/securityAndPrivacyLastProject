package ch.supsi.webapp.web.Utilities;

import ch.supsi.webapp.web.Modules.Logs;
import ch.supsi.webapp.web.Repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UtilLogger {

    private final LogsRepository logsRepository;

    public UtilLogger(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }


    private void log(String message, String level, int employeeId) {
        Logs log = new Logs();
        log.setMessage(message);
        log.setDate(new Date());
        log.setLevel(level);
        log.setEmployeeId(employeeId);

        logsRepository.save(log);
    }

    public void info(String message, int employeeId) {
        log(message, "INFO", employeeId);
    }

    public void error(String message, int employeeId) {
        log(message, "ERROR", employeeId);
    }

    public void warning(String message, int employeeId) {
        log(message, "warning", employeeId);
    }
}
