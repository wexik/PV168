package cz.muni.fi.pv168.hotel.gui;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Mashka
 */
public class DataSourceProvider {

    private static final String CONFIG_FILE = "jdbc.properties";
    private static DataSource INSTANCE = null;

    public static DataSource getDataSource() {

        if (INSTANCE == null) {
            Properties properties = new Properties();
            try {
                properties.load(DataSourceProvider.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read property file " + CONFIG_FILE, e);
            }

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl(properties.getProperty("jdbc.url"));
            dataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
            dataSource.setUsername(properties.getProperty("jdbc.user"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));

            INSTANCE = dataSource;
        }

        return INSTANCE;
    }

}
