package test.heima;

import org.heima.chat.dao.UserDao;
import org.heima.chat.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml",
									"classpath:spring-context.xml" })
@Component
public class TestUserDao {
	@Autowired
	UserDao	userDao;

	@Test
	public void testAddUser() {
		User user = new User();
		user.setAccount("test");
		user.setPassword("test");
		userDao.addUser(user);
	}

	@Test
	public void testQuery() {
		//		User user = userDao.findUserByAccount("xiaoqi");
		User user = userDao.findById("1");

		System.out.println(user.toString());
	}
}
