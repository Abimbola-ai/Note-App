package com.app.noteapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.app.noteapp.jpa.Note;
import com.app.noteapp.jpa.User;

public interface NoteRepository extends JpaRepository<Note, Integer>, PagingAndSortingRepository<Note, Integer> {
	Page<Note> findByUser(User user, Pageable pageable);

	Page<Note> findByUserNot(User user, Pageable pageable);
}
