package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({myDatabaseTest.class,myTradeTest.class,TestMain.class})
public class AllTestSuite {

}
