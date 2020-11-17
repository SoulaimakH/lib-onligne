package com.example.Ebook.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Ebook.exception.ResourceNotFoundException;
import com.example.Ebook.message.ResponseFile;
import com.example.Ebook.message.ResponseMessage;
import com.example.Ebook.model.Book;
import com.example.Ebook.model.Filebase;
import com.example.Ebook.repository.BookRepository;
import com.example.Ebook.repository.FileDBRepository;
import com.example.Ebook.service.FileStorageService;




@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v2")
public class booksController {
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private FileDBRepository fileRepository;
    
    @Autowired
    private FileStorageService storageService;
    

    @GetMapping("/books")
    public List<Book> getAllbooks() {
    	List<Book> listAllBooks = bookRepository.findAll();
    	
    	//List<Book> customList = new ArrayList<Book>();
    	List cus = new ArrayList<>();
    	
    	
    	for (final Book book : listAllBooks) {
        	HashMap<String, String> capitalCities = new HashMap<String, String>();
            capitalCities.put("id", Long.toString(book.getId()));
            capitalCities.put("title", book.gettitle());
            capitalCities.put("author", book.getauthor());
            capitalCities.put("description", book.getdescription());
            if(book.getFiledb()!= null) capitalCities.put("fileid", Long.toString(book.getFiledb().getId()));
            cus.add(capitalCities);
    	}

    	 return cus;
    }

    
    
    @GetMapping("/book/{id}")
    public HashMap<String, String>getEmployeeById(@PathVariable(value = "id") Long bookId)
        throws ResourceNotFoundException {
    	Book book = bookRepository.findById(bookId)
          .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));
    	HashMap<String, String> capitalCities = new HashMap<String, String>();
        capitalCities.put("id", Long.toString(book.getId()));
        capitalCities.put("title", book.gettitle());
        capitalCities.put("author", book.getauthor());
        capitalCities.put("description", book.getdescription());
        if(book.getFiledb()!= null) capitalCities.put("fileid", Long.toString(book.getFiledb().getId()));
        return capitalCities;
    }
    
 

    @PutMapping("/updatbook/{id}")
    public ResponseEntity<Book> updateEmployee(@PathVariable(value = "id") Long bookId,
    		/*@RequestParam("file") MultipartFile file ,*/@RequestParam("title")  String titlebook,@RequestParam("author")  
    String Authorbook,@RequestParam("desc")  String descbook) throws ResourceNotFoundException, IOException {
    	
    	Book book = bookRepository.findById(bookId)
    			
        .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));

    	//Long i = book.getFiledb().getId();
   	  book.setauthor(Authorbook);
   	  book.setdescription(descbook);
   	  book.settitle(titlebook);
   	 /*
   		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
   	   
   	  
     	  book.getFiledb().setName(fileName);
     	book.getFiledb().setType(file.getContentType());
     	book.getFiledb().setData(file.getBytes());
   	  */
        final Book updatedbook = bookRepository.save(book);
        return ResponseEntity.ok(updatedbook);
    }
    
    
    

    @DeleteMapping("/delbook/{id}")
    public Map<String, Boolean> deletebook(@PathVariable(value = "id") Long bookId)
         throws ResourceNotFoundException {
        Book book = bookRepository.findById(bookId)
       .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));
Long i=book.getFiledb().getId();
        bookRepository.delete(book);
        fileRepository.deleteById(i);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
    
    

@PostMapping("/uploadbook")
    
    public ResponseEntity<ResponseMessage> uploadFile(
    		final WebRequest webRequest,
    		@RequestParam("file") MultipartFile file ,
    		@RequestParam("title")  String titlebook,
    		@RequestParam("author")  String Authorbook,
    		@RequestParam("desc")  String descbook) {
      String message = "";
      Book book=new Book();
      try {
    	  
    			  
    	  Filebase F =  storageService.store(file);
    	  book.setauthor(Authorbook);
    	  book.setdescription(descbook);
    	  book.settitle(titlebook);
    	  book.setFiledb(F);
    	  
        bookRepository.save(book);
     
        message = "Uploaded the file successfully: "; // + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: "+e.getMessage(); // + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }




@GetMapping("/files")
public ResponseEntity<List<ResponseFile>> getListFiles() {
  List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
    String fileDownloadUri = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/files/")
        .path(Long.toString(dbFile.getId()))
        .toUriString();

    return new ResponseFile(
        dbFile.getName(),
        fileDownloadUri,
        dbFile.getType(),
        dbFile.getData().length);
  }).collect(Collectors.toList());

  return ResponseEntity.status(HttpStatus.OK).body(files);
}




@GetMapping("/file/{id}")
public ResponseEntity getFile(@PathVariable Long id) {
	 Filebase  Filebase = storageService.getFile(id);
			{
	    String fileDownloadUri = ServletUriComponentsBuilder
	        .fromCurrentContextPath()
	        .path("/files/{id}")
	        .path(Long.toString(id))
	        .toUriString();

	    ResponseFile files= new ResponseFile(
	    		 Filebase.getName(),
	        fileDownloadUri,
	        Filebase.getType(),
	        Filebase.getData().length);
	  

	  return ResponseEntity.status(HttpStatus.OK).body(files);
}
  }


@GetMapping("/filedata/{id}")

public ResponseEntity <Filebase> getFiledata(@PathVariable Long id) throws ResourceNotFoundException {
	Book book = bookRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + id));
        return ResponseEntity.ok().body(book.getFiledb());


}


@PutMapping("/updatfilebook/{id}")
public ResponseEntity<Book> updatefilbokk(@PathVariable(value = "id") Long bookId,
		@RequestParam("file") MultipartFile file ) throws ResourceNotFoundException, IOException {
	
	Book book = bookRepository.findById(bookId)
			
    .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));

	//Long i = book.getFiledb().getId();
	 
	 
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	   
	  
 	  book.getFiledb().setName(fileName);
 	book.getFiledb().setType(file.getContentType());
 	book.getFiledb().setData(file.getBytes());
	  
   final Book updatedbook = bookRepository.save(book);
    
    
    return ResponseEntity.ok(updatedbook);
}






}
