package com.tngtied.triplaner.controller;
import com.tngtied.triplaner.repository.DB;
import com.tngtied.triplaner.entity.Memo;
import com.tngtied.triplaner.dto.RequestMemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class MessageController {

    @Autowired
    public DB repo;

    @GetMapping("/memo-list")
    public Iterable<Memo> memolist() {
        Iterable<Memo> list_instance =repo.findAll();
        ArrayList<Memo> memo_list = new ArrayList<>();
        for (Memo memo : list_instance
             ) {
            if (memo.message.length()>10){
                memo.message=memo.message.substring(0, 9);
            }
            memo_list.add(memo);
        }
        return (memo_list);
    }

    @PostMapping("/memo")
    public Memo postmemo(@RequestBody RequestMemo requestmemo){
        Memo memo_instace = new Memo();
        memo_instace.message = requestmemo.message;
        memo_instace.createdate = LocalDate.now();
        memo_instace.touchdate = LocalDate.now();
        repo.save(memo_instace);
        return(memo_instace);
    }

    @GetMapping("/memo/{id}")
    public Memo getMemo(@PathVariable int id){
        return (repo.findById(id).orElse(null));
    }

    @PostMapping("/memo/{id}")
    public Memo touchmemo(@PathVariable int id, @RequestBody RequestMemo requestMemo){
        Memo memo_instance = repo.findById(id).orElse(null);
        memo_instance.touchdate = LocalDate.now();
        memo_instance.message = requestMemo.message;
        repo.save(memo_instance);
        return (memo_instance);
    }
}
