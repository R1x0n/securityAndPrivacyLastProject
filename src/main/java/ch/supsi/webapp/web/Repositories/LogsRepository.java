package ch.supsi.webapp.web.Repositories;

import ch.supsi.webapp.web.Modules.Logs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@Qualifier("repository")
public interface LogsRepository extends JpaRepository<Logs, String> {
    Logs findLogsById(Long id);
}
