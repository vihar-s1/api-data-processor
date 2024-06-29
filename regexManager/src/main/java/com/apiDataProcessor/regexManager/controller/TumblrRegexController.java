package com.apiDataProcessor.regexManager.controller;

import com.apiDataProcessor.models.InternalResponse;
import com.apiDataProcessor.regexManager.models.TumblrPattern;
import com.apiDataProcessor.regexManager.repository.TumblrPatternRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tumblr")
public class TumblrRegexController {

    final
    TumblrPatternRepository tumblrPatternRepository;

    public TumblrRegexController(TumblrPatternRepository tumblrPatternRepository) {
        this.tumblrPatternRepository = tumblrPatternRepository;
    }

    @GetMapping("/all")
    public List<TumblrPattern> getAllRegexPatterns(){
        return tumblrPatternRepository.findAll();
    }

    @PostMapping(path="/add", consumes="application/json", produces="application/json")
    public ResponseEntity<Object> addRegexExpression(@RequestBody TumblrPattern pattern) {

        // Added validation for an empty or null expression
        if (pattern.getExpression() == null || pattern.getExpression().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    InternalResponse.builder().success(false).data("Validation failed. Expression cannot be null or empty.").build()
            );
        }

        if (tumblrPatternRepository.findPatternByExpression(pattern.getExpression()) != null) {
            return ResponseEntity.status(409).body(
                    InternalResponse.builder().success(false).data("The resource you are trying to create already exists").build()
            );
        }
        TumblrPattern savedPattern = tumblrPatternRepository.save(pattern);
        return ResponseEntity.status(200).body(
                InternalResponse.builder().success(true).data(pattern).build()
        );
    }

    @DeleteMapping(path="/delete/{patternId}")
    public ResponseEntity<Object> deleteRegexExpression(@PathVariable String patternId){

        if (patternId == null || patternId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    InternalResponse.builder().success(false).data("Validation failed. PatternId cannot be null or empty.").build()
            );
        }

        Optional<TumblrPattern> pattern = tumblrPatternRepository.findById(patternId);

        if (pattern.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    InternalResponse.builder().success(false).data("Resource not found").build()
            );
        }

        tumblrPatternRepository.delete(pattern.get());
        return ResponseEntity.status(204).build();
    }

}
