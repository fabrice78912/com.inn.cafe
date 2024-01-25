package com.inn.cafe.dao;

import com.inn.cafe.POJO.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDao extends JpaRepository<Bill, Integer> {


    @Query("select b from Bill b where b.createdBy=:username order by b.id desc")
    List<Bill> getBillByUserName(@Param("username") String username);

    @Query("select b from Bill b where b.createdBy=:username order by b.id desc")
    Page<Bill> getBillByUserNamePage(@Param("username") String username, Pageable pageable);


    @Query("select b from Bill b order by b.id desc")
    List<Bill> getAllBills();

    @Query("select b from Bill b order by b.id desc")
    Page<Bill> findAllOrderedByIdDesc(Pageable pageable);
}
