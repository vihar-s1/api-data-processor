package com.VersatileDataProcessor.RegexManager.controller;

import com.VersatileDataProcessor.Models.InternalHttpResponse;
import com.VersatileDataProcessor.RegexManager.models.TumblrPattern;
import com.VersatileDataProcessor.RegexManager.repository.TumblrPatternRepository;
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
                    new InternalHttpResponse<>(false, "Validation failed. Expression cannot be null or empty.")
            );
        }

        if (tumblrPatternRepository.findPatternByExpression(pattern.getExpression()) != null) {
            return ResponseEntity.status(409).body(
                    new InternalHttpResponse<>(false, "The resource you are trying to create already exists")
            );
        }
        TumblrPattern savedPattern = tumblrPatternRepository.save(pattern);
        return ResponseEntity.status(200).body(
                new InternalHttpResponse<>(true, pattern)
        );
    }

    @DeleteMapping(path="/delete/{patternId}")
    public ResponseEntity<Object> deleteRegexExpression(@PathVariable String patternId){

        if (patternId == null || patternId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new InternalHttpResponse<>(false, "Validation failed. Pattern ID cannot be null or empty.")
            );
        }

        Optional<TumblrPattern> pattern = tumblrPatternRepository.findById(patternId);

        if (pattern.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new InternalHttpResponse<>(false, "The Resource you are trying to delete does not exist")
            );
        }

        tumblrPatternRepository.delete(pattern.get());
        return ResponseEntity.status(204).build();
    }

}
