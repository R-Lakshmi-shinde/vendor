package tester;

import static utils.HibernateUtils.getFactory;

import java.time.LocalDate;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.User;
import pojos.UserRole;

public class DeleteBulkUserDetails {

	public static void main(String[] args) {
		try (SessionFactory sf = getFactory(); Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter date n reg amount ");
			// create dao instance n test API
			UserDaoImpl dao = new UserDaoImpl();
			System.out.println(dao.deleteUserDetails(LocalDate.parse(sc.next()), sc.nextDouble()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
