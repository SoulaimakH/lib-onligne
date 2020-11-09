package com.example.Ebook.service;
import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.Ebook.model.Filebase;
import com.example.Ebook.repository.FileDBRepository;





@Service
public class FileStorageService {

	 @Autowired
	  private FileDBRepository fileDBRepository;

	  public Filebase store(MultipartFile file) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    Filebase FileDB = new Filebase(fileName, file.getContentType(), file.getBytes());
	    
	    return fileDBRepository.save(FileDB);
	  }

	  public Filebase getFile(Long id) {
	    return fileDBRepository.findById(id).get();
	  }
	  
	  public Stream<Filebase> getAllFiles() {
	    return fileDBRepository.findAll().stream();
	  }
	}