package com.inn.cafe.rest;


import com.inn.cafe.POJO.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillRest {

    @PostMapping(path = "/generateReport")
    public ResponseEntity<String> generateReport(@RequestBody(required = true) Map<String, Object> requestMap);

    @GetMapping(path = "/getBills")
    public ResponseEntity<List<Bill>> getBills();

    @PostMapping(path = "/getPdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody(required = true) Map<String, Object> requestMap);


    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable("id") Integer id);


}
