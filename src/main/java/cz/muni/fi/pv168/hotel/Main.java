package cz.muni.fi.pv168.hotel;

import org.apache.derby.jdbc.ClientDataSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * Created by wexik on 21.3.2014.
 */
public class Main {

    public static void main(String[] args) throws RoomException, IOException {
        /*ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        RoomManager roomManager = ctx.getBean(RoomManager.class);*/

        RoomManager roomManager = new RoomManagerImpl(getDateSource());

        RentManager rentManager = new RentManagerImpl(getDateSource());

        roomManager.findAllRooms().forEach(System.out::println);
        System.out.println(roomManager.findRoomById(1500));
        Room room = new Room(300, 4, new BigDecimal("25.53"));
        roomManager.createRoom(room);
    }

    /*@Configuration
    @EnableTransactionManagement
    @PropertySource("classpath:myconf.properties")
    public static class SpringConfig {

        @Autowired
        Environment env;

        @Bean
        public DataSource dataSource() {
            BasicDataSource bds = new BasicDataSource(); //Apache DBCP connection pooling DataSource
            bds.setUrl(env.getProperty("jdbc.url"));
            bds.setUsername(env.getProperty("jdbc.user"));
            bds.setPassword(env.getProperty("jdbc.password"));
            return bds;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        public RoomManager roomManager() {
            return new RoomManagerImpl(dataSource());
        }

        @Bean
        public PersonManager personManager() {
            return new PersonManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));
        }

        @Bean
        public RentManager rentManager() {
            RentManagerImpl leaseManager = new RentManagerImpl(dataSource());
            rentManager.setRoomManager(roomManager());
            rentManager.setPersonManager(personManager());
            return leaseManager;
        }
    }*/

    public static ClientDataSource getDateSource(){
        Properties myConf = new Properties();

        try {
            myConf.load(Main.class.getResourceAsStream("/jdbc.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resources");
        }

        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(myConf.getProperty("jdbc.dbname"));
        ds.setUser(myConf.getProperty("jdbc.user"));
        ds.setPassword(myConf.getProperty("jdbc.password"));

        return ds;
    }

    //ahoj
}
