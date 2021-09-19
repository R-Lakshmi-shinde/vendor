package tester;

import static utils.HibernateUtils.getFactory;

import java.time.LocalDate;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.User;
import pojos.UserRole;

public class RegisterUserGetCurntSession {

	public static void main(String[] args) {
		try (SessionFactory sf = getFactory(); Scanner sc = new Scanner(System.in)) {
			System.out.println(
					"Enter User details : name,  email,  password,  userRole,  confirmPassword,  regAmount regDate(yr-mon-day)");
			// create transient POJO : exists in heap
			User user = new User(sc.next(), sc.next(), sc.next(), UserRole.valueOf(sc.next().toUpperCase()), sc.next(),
					sc.nextDouble(), LocalDate.parse(sc.next()));
			
			user.setUserId(1234);
			System.out.println(user.getUserId());// null
			//create dao instance n test API
			UserDaoImpl dao=new UserDaoImpl();
			System.out.println(dao.registerUserWithGetCurrentSession(user));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
