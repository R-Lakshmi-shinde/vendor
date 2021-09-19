package tester;

import static utils.HibernateUtils.getFactory;

import java.time.LocalDate;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.User;
import pojos.UserRole;

public class RegisterUser {

	public static void main(String[] args) {
		try (SessionFactory sf = getFactory(); Scanner sc = new Scanner(System.in)) {
			System.out.println(
					"Enter User details : name,  email,  password,  userRole,  confirmPassword,  regAmount regDate(yr-mon-day)");
			// create transient POJO : exists in heap
			User user = new User(sc.next(), sc.next(), sc.next(), UserRole.valueOf(sc.next().toUpperCase()), sc.next(),
					sc.nextDouble(), LocalDate.parse(sc.next()));
			//create dao instance n test API
			UserDaoImpl dao=new UserDaoImpl();
			System.out.println(dao.registerUser(user));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
