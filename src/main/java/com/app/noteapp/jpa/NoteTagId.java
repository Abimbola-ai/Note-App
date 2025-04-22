package com.app.noteapp.jpa;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class NoteTagId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer noteId;
	private Integer tagId;

	public NoteTagId() {

	}

	public NoteTagId(Integer noteId, Integer tagId) {
		this.noteId = noteId;
		this.tagId = tagId;
	}

	// Getters and Setters
	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NoteTagId))
			return false;
		NoteTagId that = (NoteTagId) obj;
		return Objects.equals(noteId, that.noteId) && Objects.equals(tagId, that.tagId);

	}

	@Override
	public int hashCode() {
		return Objects.hash(noteId, tagId);
	}

}
