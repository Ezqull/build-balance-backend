import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import testapp.TestApp;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApp.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlywayMigrationTest {

    private static final String MIGRATION_PATH = "src/main/resources/migrations";
    private static Set<String> tableNames;


    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public static void setUp() throws IOException {
        tableNames = extractTableNamesFromMigrations();
    }

    @Test
    public void testMigrations_checkTableCreation() throws Exception {
        Set<String> actualTableNames = new HashSet<>();

        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.getMetaData().getTables(null, "build_balance", null, new String[]{"TABLE"});
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME").toLowerCase();
                actualTableNames.add(tableName);
            }
        }

        assertTrue(actualTableNames.containsAll(tableNames),
                "Not all migrations passed.");
    }

    private static Set<String> extractTableNamesFromMigrations() throws IOException {
        Pattern pattern = Pattern.compile("Create-table-(.*)\\.sql", Pattern.CASE_INSENSITIVE);
        Set<String> tableNames = new HashSet<>();

        try (Stream<Path> paths = Files.walk(Paths.get(MIGRATION_PATH))) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .map(File::getName)
                    .forEach(fileName -> {
                        Matcher matcher = pattern.matcher(fileName);
                        if (matcher.find()) {
                            String tableName = matcher.group(1).toLowerCase();
                            tableNames.add(tableName.replace("-", "_"));
                        }
                    });
        }
        return tableNames;
    }
}

