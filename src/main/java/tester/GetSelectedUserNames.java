package tester;

import static utils.HibernateUtils.getFactory;

import java.util.Scanner;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.UserRole;

import static java.time.LocalDate.parse;

public class GetSelectedUserNames {

	public static void main(String[] args) {
		try(SessionFactory sf=getFactory();Scanner sc=new Scanner(System.in))
		{
			System.out.println("Enter strt n end date n role ");
			//create dao instance
			UserDaoImpl dao=new UserDaoImpl();
			System.out.println("User Name List");
			dao.getSelectedUserNames
			(parse(sc.next()),parse(sc.next()),UserRole.valueOf(sc.next().toUpperCase()))
					.forEach(System.out::println);
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
