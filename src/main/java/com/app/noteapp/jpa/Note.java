package com.app.noteapp.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"Note\"")
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"noteId\"")
	private Integer noteId;

	private String noteTitle;

	@Column(columnDefinition = "TEXT")
	private String noteContent;

	private Boolean isArchived;

	private Timestamp lastEdited;

	@ManyToOne
	@JoinColumn(name = "\"userId\"", nullable = false)
	private User user;

	@OneToMany(mappedBy = "\"note\"", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<NoteTag> noteTags = new HashSet<>();

	// No-arg constructor
	public Note() {

	}

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	public Timestamp getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Timestamp lastEdited) {
		this.lastEdited = lastEdited;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", noteTitle=" + noteTitle + ", noteContent=" + noteContent + ", isArchived="
				+ isArchived + ", lastEdited=" + lastEdited + "]";
	}

}
