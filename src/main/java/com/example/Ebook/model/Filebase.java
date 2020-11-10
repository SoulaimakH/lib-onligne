package com.example.Ebook.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;





@Entity
@Table(name = "datafile")
public class Filebase implements Serializable{
	
	
	@OneToOne(
           
            mappedBy = "filedb")
	
 
private Book book;
	

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private long id;

	 
	 
	  private String name;

	  private String type;
	  @Lob
	  private byte[] data;

	  public Filebase() {
		  
	  }
	  
	  public Filebase(String name, String type, byte[] data) {
		    this.name = name;
		    this.type = type;
		    this.data = data;
		  }

	  
	public long getId() {
		return id;
	}

	public void setFile_id(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	  public byte[] getData() {
		    return data;
		  }

		public void setData(byte[] data) {
		    this.data = data;
		  }

	 
	 
}
