package com.taskmanagement.task_management_system.model;

import java.util.Objects;

public abstract class User {

	private int userId;
	private String name;
	private String email;
	private String password;

	private boolean loggedIn;

	protected User() {
	}

	protected User(int userId, String name, String email, String password) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.loggedIn = false;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
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
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Returns true when the supplied credentials match this user's stored credentials.
	 */
	public boolean login(String email, String password) {
		boolean success = Objects.equals(this.email, email) && Objects.equals(this.password, password);
		this.loggedIn = success;
		return success;
	}

	public void logout() {
		this.loggedIn = false;
	}
}
