package com.home.tayachat.config;

import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.database.DatabaseConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.util.ArrayList;
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

            boolean hasDiff = last == null ||
                    updated.stream().anyMatch(
                            changeSet -> last.parallelStream()
                                    .noneMatch(upd -> changeSet.getId().equals(upd.getId())));
            liquibaseMigrationProcessed.set(hasDiff);
        }

        private List<UpdateData> getLastUpdate(Liquibase liquibase) {
            DatabaseConnection connection = liquibase.getDatabase().getConnection();
            if (connection instanceof liquibase.database.jvm.JdbcConnection) {
                try {
                    return getLastUpdate((liquibase.database.jvm.JdbcConnection) connection);
                } catch (Exception e) {
                    appLog.warn("Failed to receive last executed update", e);
                    return null;
                }
            }
            return null;
        }

        private List<UpdateData> getLastUpdate(liquibase.database.jvm.JdbcConnection connection) throws Exception {
            ResultSet rs = connection.createStatement().executeQuery(
                    "select id, md5sum, dateexecuted from databasechangelog order by dateexecuted desc"
                            + (LIMIT > 0 ? (" limit " + LIMIT) : ""));
            if (rs == null) return null;

            List<UpdateData> list = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString(1);
                String md5 = rs.getString(2);
                Date date = rs.getDate(3);
                list.add(new UpdateData(id, md5, date));
            }

            return list;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static final class UpdateData {
        private final String id;
        private final String md5;
        private final Date date;
    }
}
