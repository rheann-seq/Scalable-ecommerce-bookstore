package com.cmu.mobilebff.controller;

import com.cmu.mobilebff.JWTHelper;
import com.cmu.mobilebff.model.Book;
import com.cmu.mobilebff.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/books")
public class BookBffController {
  private static final Logger logger = LoggerFactory.getLogger(BookBffController.class);
  BookService bookService = new BookService();

  @GetMapping({"/isbn/{ISBN}", "/{ISBN}"})
  public ResponseEntity<?> getBookByISBN(
      @PathVariable String ISBN,
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestHeader("X-Client-Type") String xClientType) {

    logger.info("Request to get book by ISBN received: " + ISBN + " xclienttype: " + xClientType);
    if (token == null || token.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
    }
    if (StringUtils.isEmpty(xClientType)
        || !(StringUtils.equalsIgnoreCase(xClientType, "Android")
            || StringUtils.equalsIgnoreCase(xClientType, "iOS"))) {
      logger.info("xclient type is missing or incorrect " + xClientType);
      return ResponseEntity.status(401).body("xClientType missing or incorrect");
    }
    // Extract the JWT token from the header and verify it
    if (StringUtils.isEmpty(token) || !JWTHelper.verifyJWTToken(token)) {
      logger.error("Empty or Invalid JWT token");
      return ResponseEntity.status(401).body("Invalid JWT Token");
    }

    ResponseEntity<?> book = bookService.findBookByIsbn(ISBN);
    logger.info("Bookservice response for isbn: " + ISBN + " " + book.toString());

    return book;
  }

  @PostMapping
  public ResponseEntity<?> addBook(
      @RequestBody Book book,
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestHeader("X-Client-Type") String xClientType) {

    logger.info("Request to add book received: " + book.toString());
    logger.info("xclient type: " + xClientType);
    if (token == null || token.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
    }
    if (StringUtils.isEmpty(xClientType)
        || !(StringUtils.equalsIgnoreCase(xClientType, "Android")
            || StringUtils.equalsIgnoreCase(xClientType, "iOS"))) {
      logger.info("xclient type is missing or incorrect " + xClientType);
      return ResponseEntity.status(401).body("xClientType missing or incorrect");
    }
    // Extract the JWT token from the header and verify it
    if (StringUtils.isEmpty(token) || !JWTHelper.verifyJWTToken(token)) {
      logger.error("Empty or Invalid JWT token");
      return ResponseEntity.status(401).body("Invalid JWT Token");
    }

    ResponseEntity<?> addedBook = bookService.addBook(book);
    logger.info("addBook service response " + addedBook.toString());
    return addedBook;
  }

  @PutMapping("/{ISBN}")
  public ResponseEntity<?> updateBook(
      @PathVariable String ISBN,
      @RequestBody Book updatedBook,
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestHeader("X-Client-Type") String xClientType) {

    logger.info("Request to update book received: " + updatedBook.toString());
    logger.info("xclient type: " + xClientType);
    if (token == null || token.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
    }
    if (StringUtils.isEmpty(xClientType)
        || !(StringUtils.equalsIgnoreCase(xClientType, "Android")
            || StringUtils.equalsIgnoreCase(xClientType, "iOS"))) {
      logger.info("xclient type is missing or incorrect " + xClientType);
      return ResponseEntity.status(401).body("xClientType missing or incorrect");
    }
    // Extract the JWT token from the header and verify it
    if (StringUtils.isEmpty(token) || !JWTHelper.verifyJWTToken(token)) {
      logger.error("Empty or Invalid JWT token");
      return ResponseEntity.status(401).body("Invalid JWT Token");
    }

    ResponseEntity<?> updatedBook1 = bookService.updateBook(ISBN, updatedBook);
    logger.info("Update book bookservice response " + updatedBook1.toString());
    return updatedBook1;
  }

  @GetMapping("/{ISBN}/related-books")
  public ResponseEntity<?> getRecommendation(
      @PathVariable String ISBN,
      @RequestHeader(value = "Authorization", required = false) String token,
      @RequestHeader("X-Client-Type") String xClientType)
      throws JsonProcessingException {
    System.out.println("getRecommendation called for ISBN : " + ISBN);

    if (token == null || token.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
    }
    if (StringUtils.isEmpty(xClientType)
        || !(StringUtils.equalsIgnoreCase(xClientType, "Android")
            || StringUtils.equalsIgnoreCase(xClientType, "iOS"))) {
      logger.info("getRecommendation :xclient type is missing or incorrect " + xClientType);
      return ResponseEntity.status(401).body("xClientType missing or incorrect");
    }
    // Extract the JWT token from the header and verify it
    if (StringUtils.isEmpty(token) || !JWTHelper.verifyJWTToken(token)) {
      logger.error("getRecommendation: Empty or Invalid JWT token");
      return bookService.getRecommendation(ISBN);
    }
    ResponseEntity<?> reccBook = bookService.getRecommendation(ISBN);
    System.out.println("get recommendation response: " + reccBook.toString());
    return reccBook;
  }
}
