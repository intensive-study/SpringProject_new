package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.QuizEntity;
import com.example.demo.exception.IdNotExistException;
import com.example.demo.service.QuizService;
import com.example.demo.service.UserQuizHistoryService;
import com.example.demo.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final UserQuizHistoryService userQuizHistoryService;

    @Autowired
    public QuizController(QuizService quizService, UserQuizHistoryService userQuizHistoryService){
        this.quizService = quizService;
        this.userQuizHistoryService = userQuizHistoryService;
    }

    @GetMapping
    public List<ResponseQuiz> getAllQuiz() {
        return this.quizService.getQuizByAll().stream()
                .map(ResponseQuiz::new).collect(Collectors.toList());
    }

    @GetMapping("/category/{categoryNum}")
    public List<ResponseQuiz> getQuizByCategory(@PathVariable("categoryNum") Long categoryNum) throws IdNotExistException {
        return this.quizService.getQuizByCategoryNum(categoryNum).stream()
                .map(ResponseQuiz::new).collect(Collectors.toList());
    }

    @GetMapping("/category")
    public List<CategoryDto> getAllCategory() {
        return this.quizService.getCategoryByAll().stream()
                .map(CategoryDto::new).collect(Collectors.toList());
    }

    @GetMapping("/{quizNum}")
    public ResponseEntity<ResponseQuiz> getQuiz(@PathVariable("quizNum") Long quizNum) throws IdNotExistException {

        QuizEntity quizEntity = quizService.getQuizByQuizNum(quizNum);
        ResponseQuiz responseQuiz = new ResponseQuiz(quizEntity);
        return ResponseEntity.status(HttpStatus.OK).body(responseQuiz);
    }

    @PostMapping()
    public ResponseEntity createQuiz(@RequestBody @Valid RequestQuiz requestQuiz) throws IdNotExistException {
        QuizEntity quizEntity = quizService.createQuiz(requestQuiz);
        ResponseQuiz responseQuiz = new ResponseQuiz(quizEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseQuiz);
    }

    @PutMapping()
    public ResponseEntity updateQuiz(@RequestBody @Valid RequestQuiz requestQuiz) throws IdNotExistException {
        //사용자 정보 변경 불가
        QuizEntity quizEntity = quizService.updateQuiz(requestQuiz);
        ResponseQuiz responseQuiz = new ResponseQuiz(quizEntity);
        System.out.println(requestQuiz.getQuizNum());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseQuiz);
    }

    @DeleteMapping("/{quizNum}")
    public ResponseEntity DeleteQuiz(@PathVariable("quizNum") Long quizNum) throws IdNotExistException {
        quizService.deleteQuiz(quizNum);
        return ResponseEntity.status(HttpStatus.OK).body("quiz id : " + quizNum + " 삭제 완료");
    }

    @PostMapping("/user/solution")
    public ResponseEntity<ResultOfUserSolutionDto> checkUserSolution(@RequestBody @Valid SubmittedUserSolutionDto userSolutionDto){
        ResultOfUserSolutionDto resultOfUserSolutionDto = userQuizHistoryService.checkUserSolution(userSolutionDto);
        if(resultOfUserSolutionDto == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        quizService.updateQuizDetailByQuizNum(userSolutionDto.getQuizNum(), resultOfUserSolutionDto.isSolved());
        return ResponseEntity.status(HttpStatus.OK).body(resultOfUserSolutionDto);
    }
}
