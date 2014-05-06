package cz.muni.fi.pv168.hotel;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by wexik on 21.3.2014.
 */
public class Main {

    public static void main(String[] args) throws RoomException, IOException, SQLException {

//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("init.sql").build();
//
//        PersonManager personManager = new PersonManagerImpl(db);

//        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        //RoomManager roomManager = ctx.getBean(RoomManager.class);

//        RoomManager roomManager = new RoomManagerImpl(getDateSource());

//        RentManager rentManager = new RentManagerImpl(getDateSource());

//        roomManager.findAllRooms().forEach(System.out::println);
//        System.out.println(roomManager.findRoomById(1500));
//        Room room = new Room(300, 4, new BigDecimal("25.53"));
//        roomManager.createRoom(room);
    }

//    static String readFile(String path) {
//        return convertStreamToString(Main.class.getClassLoader().getResourceAsStream(path));
//    }
//
//    static String convertStreamToString(java.io.InputStream is) {
//        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next() : "";
//    }
//
//    @Configuration
//    @EnableTransactionManagement
//    @PropertySource("classpath:myconf.properties")
//    public static class SpringConfig {
//
//        @Autowired
//        Environment env;
//
//        @Bean
//        public DataSource dataSource() {
//            BasicDataSource bds = new BasicDataSource(); //Apache DBCP connection pooling DataSource
//            bds.setUrl(env.getProperty("jdbc.url"));
//            bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
//            bds.setUsername(env.getProperty("jdbc.user"));
//            bds.setPassword(env.getProperty("jdbc.password"));
//            return bds;
//        }
//
//        @Bean
//        public PlatformTransactionManager transactionManager() {
//            return new DataSourceTransactionManager(dataSource());
//        }
//
//        @Bean
//        public RoomManager roomManager() {
//            return new RoomManagerImpl(dataSource());
//        }
//
//        @Bean
//        public PersonManager personManager() {
//            return new PersonManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));
//        }
//
//        @Bean
//        public RentManager rentManager() {
//            RentManagerImpl rentManager = new RentManagerImpl(dataSource());
//            rentManager.setRoomManager(roomManager());
//            rentManager.setPersonManager(personManager());
//            return rentManager;
//        }
//    }

    public static DataSource getDateSource(){
//        Properties myConf = new Properties();
//
//        try {
//            myConf.load(Main.class.getResourceAsStream("/jdbc.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load resources");
//        }
//
//        ClientDataSource ds = new ClientDataSource();
//        ds.setDatabaseName(myConf.getProperty("jdbc.dbname"));
//        ds.setUser(myConf.getProperty("jdbc.user"));
//        ds.setPassword(myConf.getProperty("jdbc.password"));
//
//        return ds;

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase build = builder.setType(EmbeddedDatabaseType.HSQL).addScript("init.sql").build();

        return build;
    }

    //ahoj
}
