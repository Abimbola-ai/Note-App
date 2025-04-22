package com.app.noteapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.noteapp.jpa.NoteTag;
import com.app.noteapp.jpa.NoteTagId;

public interface NoteTagRepository extends JpaRepository<NoteTag, NoteTagId> {

	// Get all tags linked to a note
	List<NoteTag> findByNote_NoteId(Integer noteId);

	// Get all notes linked to a tag
	List<NoteTag> findByTag_TagId(Integer tagId);

}
