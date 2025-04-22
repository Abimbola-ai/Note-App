package com.app.noteapp.jpa;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"NoteTag\"")
public class NoteTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NoteTagId id;

	@ManyToOne
	@MapsId("\"noteId\"")
	@JoinColumn(name = "\"noteId\"")
	private Note note;

	@ManyToOne
	@MapsId("\"tagId\"")
	@JoinColumn(name = "\"tagId\"")
	private Tag tag;

	public NoteTag() {

	}

	public NoteTag(Note note, Tag tag) {
		this.note = note;
		this.tag = tag;
		this.id = new NoteTagId(note.getNoteId(), tag.getTagId());
	}

	public NoteTagId getId() {
		return id;
	}

	public void setId(NoteTagId id) {
		this.id = id;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "NoteTag [id=" + id + ", note=" + note + ", tag=" + tag + "]";
	}

}
