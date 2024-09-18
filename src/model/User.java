package model;

public class User {
	protected int idUser;
	protected String username;
	protected String password;
	
	public User() {
		
	}
	
	public User(int idUser, String username, String password) {
		this.idUser = idUser;
		this.username = username;
		this.password = password;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public int getId() {
		return idUser;
	}
	
	@Override
	public String toString() {
		return "User [id=" + idUser + ", username=" + username + ", password=" + password + "]";
	}
	
}
