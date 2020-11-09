package com.example.Ebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ebook.model.Book;



public interface BookRepository extends JpaRepository<Book, Long> {

}
