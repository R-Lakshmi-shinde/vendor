package pojos;

import java.time.LocalDate;

import javax.persistence.*;

@Entity // mandatory class level annotation
@Table(name = "users_tbl") // specifies table name
public class User {// Hibernate DOES NOT mandate that POJO should be Serializable
	/*
	 * userId (PK) ,name,email,password,role(enum),confirmPassword, regAmount;
	 * LocalDate/Date regDate; byte[] image;
	 */
	@Id // mandatory : PK constraint (Unique ID prop => PK --MUST be Serializable)
//	@GeneratedValue //Hibernate chooses default DB specific strategy for auto PK generation
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment constraint suitable for mysql
	@Column(name = "user_id")
	private Integer userId;
	@Column(length = 20) // varchar(20)
	private String name;
	@Column(length = 20, unique = true) // varchar(20) , unique constraint
	private String email;
	@Column(length = 15, nullable = false) // varchar(20) ,NOT NULL
	private String password;
	@Enumerated(EnumType.STRING) // col type varchar(20)
	@Column(name = "user_role", length = 20)
	private UserRole userRole;
	@Transient // skips from persistence (no corresponding column)
	private String confirmPassword;
	@Column(name = "reg_amount")
	private double regAmount;
	@Column(name = "reg_date")
	private LocalDate regDate;// column : date
	@Lob // column type blob : longblob
	private byte[] image;

	// must supply def constr.
	public User() {
		System.out.println("in user ctor");
	}

	public User(String name, String email, String password, UserRole userRole, String confirmPassword, double regAmount,
			LocalDate regDate) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.userRole = userRole;
		this.confirmPassword = confirmPassword;
		this.regAmount = regAmount;
		this.regDate = regDate;
	}

	// email , regAmount , regDate
	public User(String email, double regAmount, LocalDate regDate) {
		super();
		this.email = email;
		this.regAmount = regAmount;
		this.regDate = regDate;
	}

	// ALL setters n getters
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		System.out.println("in set email");
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public double getRegAmount() {
		return regAmount;
	}

	public void setRegAmount(double regAmount) {
		this.regAmount = regAmount;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", userRole=" + userRole + ", confirmPassword=" + confirmPassword + ", regAmount=" + regAmount
				+ ", regDate=" + regDate;
	}

}
