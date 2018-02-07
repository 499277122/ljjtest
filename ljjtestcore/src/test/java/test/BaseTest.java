package test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext.xml" })
public class BaseTest {
	private long startTime;
	private long endTime;

	@Before
	public void before() {
		startTime = System.currentTimeMillis();
		System.out.println("============before==============");
	}

	@After
	public void after() {
		endTime = System.currentTimeMillis();
		System.out.println("=========耗时:" + (endTime - startTime) + "============");
		System.out.println("============after==============");//
	}
	
	/*@Test
	public void go(){
		System.out.println("");
	}*/
}
