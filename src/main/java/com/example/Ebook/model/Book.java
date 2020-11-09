package com.example.Ebook.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;



@Entity
@Table(name = "book")
public class Book implements Serializable{
	
	

	 @OneToOne
	  @JoinColumn(name = "datafile_id",nullable = false)
	
	private Filebase filedb;
	 
	 
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long id;
	 
	 
	 
	    private String title;
	    private String author;
	    private String description;
	 
	    public Book() {
	  
	    }
	 
	    public Book(String title, String author, String description) {
	         this.title = title;
	         this.author = author;
	         this.description = description;
	    }
	 
	   
	        public long getId() {
	        return id;
	    }
	    public void setId(long id) {
	        this.id = id;
	    }
	 
	    @NotBlank
	    @Column(name = "title")
	    public String gettitle() {
	        return title;
	    }
	    public void settitle(String title) {
	        this.title = title;
	    }
	 
	    @Column(name = "author", nullable = false)
	    public String getauthor() {
	        return author;
	    }
	    public void setauthor(String author) {
	        this.author = author;
	    }
	 
	    @Column(name = "description", nullable = false)
	    public String getdescription() {
	        return description;
	    }
	    public void setdescription(String description) {
	        this.description = description;
	    }

		public Filebase getFiledb() {
			return filedb;
		}

		public void setFiledb(Filebase filedb) {
			this.filedb = filedb;
		}
	    
}
