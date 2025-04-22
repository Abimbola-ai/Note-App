package com.app.noteapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.noteapp.jpa.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	Optional<Tag> findByTag(String tag);

}
