package com.VersatileDataProcessor.RegexManager.controller;

import com.VersatileDataProcessor.RegexManager.models.MyResponseBody;
import com.VersatileDataProcessor.RegexManager.models.TumblrPattern;
import com.VersatileDataProcessor.RegexManager.repository.TumblrPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tumblr")
public class TumblrRegexController {

    @Autowired
    TumblrPatternRepository tumblrPatternRepository;

    @GetMapping("/all")
    public List<TumblrPattern> getAllRegexPatterns(){
        return tumblrPatternRepository.findAll();
    }

    @PostMapping(path="/add", consumes="application/json", produces="application/json")
    public ResponseEntity<Object> addRegexExpression(@RequestBody TumblrPattern pattern) {

        if (tumblrPatternRepository.findPatternByExpression(pattern.getExpression()) != null) {
            return ResponseEntity.status(409).body(
                    new MyResponseBody<TumblrPattern>("The resource you are trying to create already exists", false, pattern)
            );
        }

        return ResponseEntity.status(200).body(
                tumblrPatternRepository.save(pattern)
        );
    }

    @DeleteMapping(path="/delete/{patternId}")
    public ResponseEntity<Object> deleteRegexExpression(@PathVariable String patternId){
        Optional<TumblrPattern> pattern = tumblrPatternRepository.findById(patternId);

        if (pattern.isEmpty()){
            return ResponseEntity.status(404).body(
                    new MyResponseBody<String>("The Resource you are trying to delete does not exist", false, patternId)
            );
        }

        tumblrPatternRepository.delete(pattern.get());
        return ResponseEntity.status(204).build();
    }

}
