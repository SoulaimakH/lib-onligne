package com.example.Ebook.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Ebook.exception.ResourceNotFoundException;
import com.example.Ebook.message.ResponseFile;
import com.example.Ebook.message.ResponseMessage;
import com.example.Ebook.model.Book;
import com.example.Ebook.model.Filebase;
import com.example.Ebook.repository.BookRepository;
import com.example.Ebook.service.FileStorageService;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v2")
public class booksController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FileStorageService storageService;

    @GetMapping("/books")
    public List<Book> getAllEmployees() {
    	 return bookRepository.findAll();
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getEmployeeById(@PathVariable(value = "id") Long bookId)
        throws ResourceNotFoundException {
    	Book book = bookRepository.findById(bookId)
          .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));
        return ResponseEntity.ok().body(book);
    }
    
    @PostMapping("/addbook")
    public Book createbook( @RequestBody Book book) {
        return bookRepository.save(book);
        
        
        
    }

    @PutMapping("/updatbook/{id}")
    public ResponseEntity<Book> updateEmployee(@PathVariable(value = "id") Long bookId,
         @Valid @RequestBody Book bookDetails) throws ResourceNotFoundException {
    	Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + bookId));

    	book.setdescription(bookDetails.getdescription());
    	book.settitle(bookDetails.gettitle());
    	book.setauthor(bookDetails.getauthor());
        final Book updatedbook = bookRepository.save(book);
        return ResponseEntity.ok(updatedbook);
    }

    @DeleteMapping("/delbook/{id}")
    public Map<String, Boolean> deletebook(@PathVariable(value = "id") Long bookId)
         throws ResourceNotFoundException {
        Book book = bookRepository.findById(bookId)
       .orElseThrow(() -> new ResourceNotFoundException("book not found for this id :: " + bookId));

        bookRepository.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
    
    

@PostMapping("/uploadfile")
    
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file ,@RequestParam("title")  String titlebook,@RequestParam("author")  String Authorbook,@RequestParam("desc")  String descbook) {
      String message = "";
      Book book=new Book();
      try {
    	  
    			  
    	  Filebase F =  storageService.store(file);
    	  book.setauthor(Authorbook);
    	  book.setdescription(descbook);
    	  book.settitle(titlebook);
    	  book.setFiledb(F);
    	  
        bookRepository.save(book);
     
        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }


    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
    	Filebase fileDB = storageService.getFile(id);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
          .body(fileDB.getData());
    }
  }

