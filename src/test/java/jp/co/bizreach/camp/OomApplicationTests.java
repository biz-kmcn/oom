package jp.co.bizreach.camp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OomApplication.class)
public class OomApplicationTests {

	@Autowired
	private OmiyageOiteokimashitaMail oom;

	@Test
	public void execute() {
		oom.execute();
	}

}
