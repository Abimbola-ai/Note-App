package com.app.noteapp.exception.domain;

public class NoteNotUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoteNotUserException(String message) {
		super(message);

	}
}
