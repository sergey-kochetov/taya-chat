package com.home.tayachat.config;

import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class LiquibaseConfiguration {
    public static final AtomicBoolean liquibaseMigrationProcessed = new AtomicBoolean(false);

    public static boolean isModelUpdated() {
        return liquibaseMigrationProcessed.get();
    }

    @Bean
    public SpringLiquibase liquibase(javax.sql.DataSource dataSource) {
        RiotSpringLiquibase liquibase = new RiotSpringLiquibase(log);
        liquibase.setChangeLog("classpath:taya-changelog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @RequiredArgsConstructor
    public static class RiotSpringLiquibase extends SpringLiquibase {
        private static final int LIMIT = -1;
        private final org.slf4j.Logger appLog;

        @Override
        protected void performUpdate(Liquibase liquibase) throws LiquibaseException {
            List<UpdateData> last = getLastUpdate(liquibase);
            super.performUpdate(liquibase);
            List<ChangeSet> updated = liquibase.getDatabaseChangeLog().getChangeSets();

            boolean hasDiff = last.isEmpty() ||
                    updated.stream().anyMatch(
                            changeSet -> last.stream().noneMatch(upd -> changeSet.getId().equals(upd.getId())));
            liquibaseMigrationProcessed.set(hasDiff);
        }

        private List<UpdateData> getLastUpdate(Liquibase liquibase) {
            DatabaseConnection connection = liquibase.getDatabase().getConnection();
            if (connection instanceof JdbcConnection) {
                try {
                    return getLastUpdate((JdbcConnection) connection);
                } catch (Exception e) {
                    appLog.warn("Failed to receive last executed update", e);
                }
            }
            return Collections.emptyList();
        }

        private List<UpdateData> getLastUpdate(JdbcConnection connection) throws Exception {
            ResultSet rs = connection.createStatement().executeQuery(
                    "select id, md5sum, dateexecuted from databasechangelog order by dateexecuted desc"
                            + (LIMIT > 0 ? (" limit " + LIMIT) : ""));
            if (rs == null) return null;

            List<UpdateData> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new UpdateData(
                        rs.getString("id"),
                        rs.getString("md5sum"),
                        rs.getDate("dateexecuted")
                ));
            }
            return list;
        }
    }

    @AllArgsConstructor
    @Getter
    public static final class UpdateData {
        private final String id;
        private final String md5;
        private final Date date;
    }
}
