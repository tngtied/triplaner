package com.tngtied.triplaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        memo_instace.createdate = new Date();
        memo_instace.touchdate = new Date();
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
        memo_instance.touchdate = new Date();
        memo_instance.message = requestMemo.message;
        repo.save(memo_instance);
        return (memo_instance);
    }
}
