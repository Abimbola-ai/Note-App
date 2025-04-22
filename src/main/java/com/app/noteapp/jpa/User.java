package com.app.noteapp.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"User\"")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"userId\"")
	private Integer userId;

	@Column(name = "\"firstName\"")
	private String firstName;

	@Column(name = "\"lastName\"")
	private String lastName;

	@Column(name = "\"username\"")
	private String username;

	@Column(name = "\"email\"")
	private String email;

	@Column(name = "\"password\"")
	@JsonProperty(access = Access.WRITE_ONLY) // Field not to be included when converting to JSON during serialization
	private String password;

	@Column(name = "\"emailVerified\"")
	private boolean emailVerified;

	@Column(name = "\"createdOn\"")
	private Timestamp createdOn;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Note> notes;

	// Default Constructor
	protected User() {
	}

	// Getters and Setters
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", username="
				+ username + ", email=" + email + ", password=" + password + ", emailVerified=" + emailVerified
				+ ", createdOn=" + createdOn + "]";
	}

}
