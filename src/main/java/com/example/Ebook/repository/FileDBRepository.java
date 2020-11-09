package com.example.Ebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ebook.model.Filebase;



public interface FileDBRepository extends JpaRepository<Filebase, Long> {

}
